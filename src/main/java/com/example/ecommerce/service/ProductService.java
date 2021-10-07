package com.example.ecommerce.service;

import com.example.ecommerce.co.RequestCO;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.repo.ProductRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;


@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestHighLevelClient client;

    public Map<String, Long> getCounts(String field) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder aggregation = AggregationBuilders.terms(field)
                .field(field).order(BucketOrder.count(false)).size(5);

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return convertAggTermHitsToDTO(response, field);
    }

    public Map<String, Long> getPriceRange() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggregation = AggregationBuilders.filters("price_range",
            new FiltersAggregator.KeyedFilter("<500", QueryBuilders.rangeQuery("variant_price").lt(500)),
            new FiltersAggregator.KeyedFilter("500-1000", QueryBuilders.rangeQuery("variant_price").gte(500).lt(1000)),
            new FiltersAggregator.KeyedFilter("1000-2000", QueryBuilders.rangeQuery("variant_price").gte(1000).lt(2000)),
            new FiltersAggregator.KeyedFilter(">2000", QueryBuilders.rangeQuery("variant_price").gte(2000)));

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return convertAggRangeHitsToDTO(response);
    }

    public List<ProductDTO> getProducts(RequestCO requestCO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(StringUtils.hasLength(requestCO.getSearchString())){
            boolQueryBuilder.must(getMultiMatchQueryBuilder(requestCO.getSearchString()));
        } else {
            boolQueryBuilder.must(getMatchAllQueryBuilder());
        }

        if(StringUtils.hasLength(requestCO.getSize()) && !requestCO.getSize().equalsIgnoreCase("size")){
            boolQueryBuilder.must(getMatchQueryBuilder(requestCO.getSize(), "size"));
        }

        if(StringUtils.hasLength(requestCO.getGender()) && !requestCO.getGender().equalsIgnoreCase("gender")){
            boolQueryBuilder.must(getMatchQueryBuilder(requestCO.getGender(), "ideal_for"));
        }

//        if(Objects.nonNull(requestCO.getColor()) && !requestCO.getColor().isEmpty()){
//            boolQueryBuilder.must(getMatchQueryBuilderWithList(requestCO.getColor(), "dominant_color"));
//        }

        searchSourceBuilder.query(boolQueryBuilder);

        if(StringUtils.hasLength(requestCO.getSortBy())){
            switch (requestCO.getSortBy()){
                case "asc" : searchSourceBuilder.sort(getSortBuilder("variant_price", SortOrder.ASC)); break;
                case "desc" : searchSourceBuilder.sort(getSortBuilder("variant_price", SortOrder.DESC)); break;
                case "new" : searchSourceBuilder.sort(getSortBuilder("variant_price", SortOrder.ASC)); break;
                case "rating" : searchSourceBuilder.sort(getSortBuilder("variant_price", SortOrder.ASC)); break;
            }
        }

        if(StringUtils.hasLength(requestCO.getSearchString())){
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .postTags("</em>")
                    .preTags("<em>")
                    .field("title");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        searchSourceBuilder.size(50);
        searchRequest.source(searchSourceBuilder);
        searchRequest.preference("1");
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return convertHitsToDTO(response);
    }

    private QueryBuilder getMatchAllQueryBuilder(){
        return QueryBuilders.matchAllQuery();
    }

    private QueryBuilder getMatchQueryBuilder(String searchString, String field){

        return QueryBuilders.matchQuery(field, searchString).fuzziness(Fuzziness.AUTO);
    }

    private QueryBuilder getMatchQueryBuilderWithList(List<String> searchString, String field){

        return QueryBuilders.matchQuery(field, searchString).fuzziness(Fuzziness.AUTO);
    }

    private QueryBuilder getMultiMatchQueryBuilder(String searchString){

        return QueryBuilders.multiMatchQuery(searchString, "product_type", "ideal_for");
    }

    private QueryBuilder getBoolQueryBuilder(String searchString){
        return QueryBuilders.boolQuery().must(getMultiMatchQueryBuilder(searchString));
    }

    private SortBuilder<FieldSortBuilder> getSortBuilder(String sortBy, SortOrder sortOrder){
        return SortBuilders.fieldSort(sortBy).order(sortOrder);
    }

    private List<ProductDTO> convertHitsToDTO(SearchResponse response){
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            ProductDTO productDTO = new ProductDTO();
            Map source = hit.getSourceAsMap();
            if (source.get("title") != null)
                productDTO.setTitle((String) source.get("title"));
            if (source.get("variant_price") != null)
                productDTO.setVariantPrice(Long.valueOf((Integer) source.get("variant_price")));
            if (source.get("brand") != null)
                productDTO.setBrand((String) source.get("brand"));
            if (source.get("size") != null)
                productDTO.setSize((String) source.get("size"));
            if (source.get("ideal_for") != null)
                productDTO.setGender((String) source.get("ideal_for"));
            if (source.get("dominant_color") != null)
                productDTO.setColor((String) source.get("dominant_color"));
            if (source.get("images") != null) {
                String images = (String) source.get("images");
                String[] image = images.split("\\|");
                productDTO.setImage(image[0]);
            }
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    private Map<String, Long> convertAggTermHitsToDTO(SearchResponse response, String agg){
        Terms genders = response.getAggregations().get(agg);
        Map<String, Long> count = new HashMap<>();
        for (Terms.Bucket entry : genders.getBuckets()) {
            count.put(entry.getKeyAsString(), entry.getDocCount());
        }
        return sortByComparator(count);
    }

    private Map<String, Long> convertAggRangeHitsToDTO(SearchResponse response){
        Map<String, Long> count = new HashMap<>();
        Filters agg = response.getAggregations().get("price_range");
        for (Filters.Bucket entry : agg.getBuckets()) {
            count.put(entry.getKeyAsString(), entry.getDocCount());
        }
        return count;
    }

    private static Map<String, Long> sortByComparator(Map<String, Long> unsortMap) {

        List<Map.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}

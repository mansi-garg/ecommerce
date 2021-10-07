package com.example.ecommerce.repo;

import com.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    @Query("{\n" +
            "    \"match\": {\n" +
            "      \"brand\": \"?0\"\n" +
            "    }\n" +
            "  }")
    List<Product> findAllByBrand(String brand);

    @Transactional(readOnly = true)
    Page<Product> findAll(Pageable pageable);

    @Query("{\n" +
            "    \"bool\": {\n" +
            "      \"should\": [\n" +
            "        {\n" +
            "          \"match\": {\n" +
            "            \"product_type\": \"?0\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"match\": {\n" +
            "            \"ideal_for\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }")
    List<Product> findBySearchQuery(String searchString);


    @Query("{\n" +
            "  \"sort\":\n" +
            "    { \"variant_price\": \"asc\" }\n" +
            "}")
    List<Product> findByPriceAsc();

    @Query("{\n" +
            "  \"sort\":\n" +
            "    { \"variant_price\": \"desc\" }\n" +
            "}")
    List<Product> findByPriceDesc();

}

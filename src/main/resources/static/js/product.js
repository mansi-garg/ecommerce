
$(document).ready(function () {
    populateTable("/products")
});

function loadSearch() {
    populateTable("/products");
}

function sortBy() {
    populateTable("/products");
}

function genderChange() {
    populateTable("/products");
}

function sizeChange() {
    populateTable("/products");
}

function changeColor() {
    populateTable("/products");
}

var productTable;

function populateTable(uri) {
    var colors = [];
    var prices = [];
    var brands = [];
    $('.color:checked').each(function(){
        var color = $(this).val();
        if(!colors.includes(color)){
            colors.push(color);
        }
    });
    $('.price:checked').each(function(){
        var price = $(this).val();
        if(!prices.includes(price)){
            prices.push(price);
        }
    });
    $('.brand:checked').each(function(){
        var brand = $(this).val();
        if(!brands.includes(brand)){
            brands.push(brand);
        }
    });
    productTable = $("#productTable").DataTable({
        searching: false,
        bLengthChange: false,
        destroy: true,
        ajax: {
            url: uri,
            type: "POST",
            dataSrc : "",
            contentType: "application/json; charset=utf-8",
            data: function (data) {
                data.searchString = $(".search").val();
                data.sortBy = $("#sort").val();
                data.size = $("#size").val();
                data.gender = $("#gender").val();
                data.color = colors;
                data.price = prices;
                data.brand = brands;
                return data = JSON.stringify(data);
            }
        },
        columns: [
            {
                orderable: false,
                data: null,
                swidth: '19%',
                render: function (data) {
                    var htmlString = "<a>";
                    htmlString += "<img width='60' src='" + data.image + "'/> &nbsp;&nbsp;";
                    htmlString += "</a>";
                    return htmlString;
                }
            },
            {
                data: "title"
            },
            {
                data: "color"
            },
            {
                data: "size"
            },
            {
                data: "gender"
            },
            {
                data: "variantPrice"
            },
            {
                data: "brand"
            }
        ]

    });
}
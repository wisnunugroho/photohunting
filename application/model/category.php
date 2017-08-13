<?php 

class Category {

    public static
    function category_get_all() {
        $sql = "
            SELECT
                category.id_category AS categoryId,
                category.category_name AS categoryName,
                category.category_description AS categoryDescription
            FROM
                category";

        CRUD::execute($sql, CRUD::$SELECT);
    }
}
?>
<?php 
/**
 * Send debug code to the Javascript console
 */
class Console {
    public static function observe($data) {
        if (is_array($data) || is_object($data)) {
            echo("<script>console.log('PHP: ".json_encode($data)."');</script>");
        } else {
            echo("<script>console.log('PHP: ".$data."');</script>");
        }
    }
}
?>
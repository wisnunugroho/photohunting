<?php 

class User {
      
  public static
  function user_login($email, $password) {
    $sql = "SELECT
                id_user AS userId,
                `name` AS userName,
                email AS userEmail,
                `password` AS userPassword,
                photo AS userPhotoProfile,
                `status` AS userStatus,
                status_date AS userStatusDate,
                registered AS userSince
            FROM
                USER
            WHERE
                email = '$email' AND
                password = '$password'";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function user_register($name, $email, $password, $photo) {
    $status = "Hi, Im on Spot Photo Hunting Now";

    $sql = "INSERT INTO user (name, email, password, photo, status)
                VALUES ('$name', '$email', '$password', '$photo', '$status')";

    CRUD::execute($sql, CRUD::$INSERT);
  }

  public static
  function user_update_status($uid, $status, $date) {
    $sql = "UPDATE user SET
                    status = '$status',
                    status_date = '$date'
                WHERE 
                    id_user = '$uid'";

    CRUD::execute($sql, CRUD::$UPDATE);
  }
}
?>
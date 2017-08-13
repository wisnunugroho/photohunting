<?php 

class Photo {

  public static
  function photo_get_all() {
    $sql = "SELECT
                    photo.id_photo AS photoID,
                    photo.name AS photoName,
                    photo.description AS photoDescription,
                    photo.url AS photoUrl,
                    photo.location AS photoLocation,
                    photo.latitude AS photoLatitude,
                    photo.longitude AS photoLongitude,
                    DATE_FORMAT(photo.date_take,'%d %M %Y') AS photoDate,
                    photo.total_like AS photoTotalLike,
                    photo.total_comment AS photoTotalComment,
                    category_name AS photoCategory,
                    `user`.`name` AS photoBy
                FROM
                    photo
                INNER JOIN `user` ON photo.id_user = `user`.id_user
                INNER JOIN category ON photo.id_category = category.id_category
                ORDER BY
                    photo.date_take DESC";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_get_with_uid($id) {
    $sql = "SELECT
                    photo.id_photo AS photoID,
                    photo. NAME AS photoName,
                    photo.description AS photoDescription,
                    photo.url AS photoUrl,
                    photo.location AS photoLocation,
                    photo.latitude AS photoLatitude,
                    photo.longitude AS photoLongitude,
                    photo.date_take AS photoDate,
                    photo.total_like AS photoTotalLike,
                    photo.total_comment AS photoTotalComment,
                    category_name AS photoCategory,
                    `user`.`name` AS photoBy
                FROM
                    photo
                INNER JOIN `user` ON photo.id_user = `user`.id_user
                INNER JOIN category ON photo.id_category = category.id_category
                WHERE
                    photo.id_user = $id
                ORDER BY
                    photoDate DESC";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_get_with_pid($id) {
    $sql = "SELECT
                    photo.id_photo AS photoID,
                    photo. NAME AS photoName,
                    photo.description AS photoDescription,
                    photo.url AS photoUrl,
                    photo.location AS photoLocation,
                    photo.latitude AS photoLatitude,
                    photo.longitude AS photoLongitude,
                    photo.date_take AS photoDate,
                    photo.total_like AS photoTotalLike,
                    photo.total_comment AS photoTotalComment,
                    category_name AS photoCategory,
                    `user`.`name` AS photoBy
                FROM
                    photo
                INNER JOIN `user` ON photo.id_user = `user`.id_user
                INNER JOIN category ON photo.id_category = category.id_category
                WHERE
                photo.id_photo = $id";

    CRUD::execute($sql, CRUD::$SELECT);
  }
  
  public static
  function photo_get_with_cid($cid) {
    $sql = "SELECT
                    photo.id_photo AS photoID,
                    photo. NAME AS photoName,
                    photo.url AS photoUrl,
                    photo.location AS photoLocation,
                    photo.latitude AS photoLatitude,
                    photo.longitude AS photoLongitude,
                    photo.description As photoDescription,
                    `user`.`name` AS photoBy
                FROM
                    photo
                INNER JOIN `user` ON photo.id_user = `user`.id_user
                INNER JOIN category ON photo.id_category = category.id_category
                WHERE
                photo.id_category = $cid";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_get_likes_with_uid($uid){
    $sql = "SELECT
                photo_like_details.id_user,
                photo_like_details.id_photo
            FROM
                photo_like_details
            WHERE
                photo_like_details.id_user = '$uid'";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_get_likes_with_pid($id) {
    $sql = "SELECT
                DATE_FORMAT(photo_like_details.like_date,'%d %M %Y') AS like_date,
                `user`.`name`
            FROM
                photo_like_details
            INNER JOIN `user` ON photo_like_details.id_user = `user`.id_user
            WHERE
                photo_like_details.id_photo = '$id'
            ORDER BY
                photo_like_details.like_date DESC";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_get_comments_with_pid($id) {
    $sql = "SELECT
                DATE_FORMAT(photo_comment_details.comment_date,'%d %M %Y') AS comment_date,
                `user`.`name`,
                photo_comment_details.comment_content
            FROM
                photo_comment_details
            INNER JOIN `user` ON photo_comment_details.id_user = `user`.id_user
            WHERE
                photo_comment_details.id_photo = '$id'
            ORDER BY
                photo_comment_details.comment_date DESC";

    CRUD::execute($sql, CRUD::$SELECT);
  }

  public static
  function photo_insert_new($name, $description, $url, $location, $latitude, $longitude, $dateTake, $uid, $cid) {
    $sql = "INSERT INTO photo(name, description, url, location, latitude, longitude, date_take, id_user, id_category)
               VALUES ('$name', '$description', '$url', '$location', '$latitude','$longitude', '$dateTake', '$uid', '$cid')";

    CRUD::execute($sql, CRUD::$INSERT);
  }

  public static
  function photo_insert_new_like($date, $pid, $uid) {
    $sql = "INSERT INTO photo_like_details(like_date, id_user, id_photo)
               VALUES ('$date', '$uid', '$pid')";

    $connection = CRUD::newConnection();
    $executor = $connection -> query($sql);
    if ($connection -> affected_rows > 0) {
      CRUD::success("Berhasil menambahkan like");
    } else {
      CRUD::error("Tidak dapat menambahkan like lagi");
    }

    $sql = "UPDATE photo
                SET total_like = (
                    SELECT
                        Count(id_photo)
                    FROM
                        photo_like_details
                    WHERE
                        id_photo = '$pid'
                )
                WHERE
                    id_photo = '$pid'";
    $executor = $connection -> query($sql);
  }

  public static
  function photo_insert_new_comment($comment, $date, $pid, $uid) {
    $sql = "INSERT INTO photo_comment_details(comment_content, comment_date, id_photo, id_user)
               VALUES ('$comment', '$date', '$pid', '$uid')";

    $connection = CRUD::newConnection();
    $executor = $connection -> query($sql);
    if ($connection -> affected_rows > 0) {
      CRUD::success("Berhasil menambahkan comment");
    } else {
      CRUD::error("Gagal menambahkan comment");
    }

    $sql = "UPDATE photo
                 SET total_comment = (
                        SELECT
                            Count(
                                photo_comment_details.id_photo
                            )
                        FROM
                            photo_comment_details
                        WHERE
                            photo_comment_details.id_photo = '$pid'
                    )
                 WHERE
                    id_photo = '$pid'";
    $executor = $connection -> query($sql);
  }

public static
  function photo_delete($pid) {
    $sql = "DELETE FROM photo
                   WHERE id_photo ='$pid'";

    $connection = CRUD::newConnection();
    $executor = $connection -> query($sql);

    if ($connection -> affected_rows > 0) {
      CRUD::success("Berhasil menghapus photo");
    } else {
      CRUD::error("Tidak ada photo sesuai kriteria");
    }
  }
  
  public static
  function photo_delete_like($uid, $pid) {
    $sql = "DELETE FROM photo_like_details
                   WHERE id_user='$uid' AND id_photo='$pid'";

    $connection = CRUD::newConnection();
    $executor = $connection -> query($sql);

    if ($connection -> affected_rows > 0) {
      CRUD::success("Berhasil menghapus like");
    } else {
      CRUD::error("Tidak ada like sebelumnya");
    }

    $sql = "UPDATE photo
                SET total_like = (
                    SELECT
                        Count(id_photo)
                    FROM
                        photo_like_details
                    WHERE
                        id_photo = '$pid'
                )
                WHERE
                    id_photo = '$pid'";

    $executor = $connection -> query($sql);

  }

  public static
  function photo_delete_comment($uid, $pid) {

    $sql = "DELETE FROM photo_comment_details
                   WHERE id_user='$uid' AND id_photo='$pid'";

    $connection = CRUD::newConnection();
    $executor = $connection -> query($sql);
    if ($connection -> affected_rows > 0) {
      CRUD::success("Berhasil menghapus comment");
    } else {
      CRUD::error("Tidak ada comment sesuai kriteria");
    }

    $sql = "UPDATE photo
                 SET total_comment = (
                        SELECT
                            Count(
                                photo_comment_details.id_photo
                            )
                        FROM
                            photo_comment_details
                        WHERE
                            photo_comment_details.id_photo = '$pid'
                 )
                 WHERE
                    id_photo = '$pid'";
                    
    $executor = $connection -> query($sql);
  }
  
  public static
  function check_is_there_any_file_uploaded($photoImage){
    $target_dir = "photos/";
    $target_file = $target_dir . basename($_FILES["photoImage"]["name"]);
    $uploadOk = 1;
    $imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);
    
    // Check if image file is a actual image or fake image
    if(isset($photoImage)) {
        $check = getimagesize($_FILES["photoImage"]["tmp_name"]);
        if($check !== false) {
            // Console::observe("File is an image - " . $check["mime"] . ".");
            $uploadOk = 1;
        } else {
            // Console::observe("File is not an image.");
            $uploadOk = 0;
        }
    }
    
    // Check if file already exists
    if (file_exists($target_file)) {
        // Console::observer("Sorry, file already exists.");
        CRUD::error("failed to execute insert data. File already exist");
        $uploadOk = 0;
    }
    
    // Check file size
    if ($_FILES["photoImage"]["size"] > 500000) {
        // Console::observer("Sorry, your file is too large.");
        CRUD::error("failed to execute insert data. File too large");
        $uploadOk = 0;
    }
    
    // Check if $uploadOk is set to 0 by an error
    if ($uploadOk == 0) {
        Console::observer("Maaf, file ini pernah diuplad sebelumnya.");
    } else {
        if (move_uploaded_file($_FILES["photoImage"]["tmp_name"], $target_file)) {
            // Console::observe("The file ". basename( $_FILES["fileToUpload"]["name"]). " berhasil diupload.");
            CRUD::success("insert is successfully execute");
        } else {
            // Console::observe("Sorry, there was an error uploading your file.");
            CRUD::error("failed to execute insert data");
        }
    }
  }
}
?>
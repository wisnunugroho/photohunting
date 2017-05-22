<?php 
error_reporting(E_ALL);
require_once 'system/console.php';
require_once 'system/database.php';
require_once 'system/crud.php';
require_once 'model/user.php';
require_once 'model/category.php';
require_once 'model/photo.php';
date_default_timezone_set("Asia/Jakarta");

$api = new API();
$api->preparePost();
$api->request($_POST['request']);

class API {
    private $uid;
    private $userName;
    private $userEmail;
    private $userPassword;
    private $userPhoto;
    private $userStatus;
    private $userStatusDate;
    private $userRegistered;

    private $pid;
    private $photoName;
    private $photoImage;
    private $photoDescription;
    private $photoUrl;
    private $photoLocation;
    private $photoLatitude;
    private $photoLongitude;
    private $photoComment;
    private $today;
        
    private $cid;

     
    function __construct() {
        $this->today = date('Y-m-d H:i:s');
    }

    function preparePost() {
        foreach($_POST as $param_name => $param_value) {
            switch ($param_name) {
                case "uid";
                $this->uid = $param_value;
                break;
                case "userName";
                $this->userName = $param_value;
                break;
                case "userEmail";
                $this->userEmail = $param_value;
                break;
                case "userPassword";
                $this->userPassword = $param_value;
                break;
                case "userPhoto";
                $this->userPhoto = $param_value;
                break;
                case "userStatus";
                $this->userStatus = $param_value;
                break;
                case "userStatusDate";
                $this->userStatusDate = $param_value;
                break;
                case "userRegistered";
                $this->userRegistered = $param_value;
                break;

                case "pid";
                $this->pid = $param_value;
                break;
                case "photoName";
                $this->photoName = $param_value;
                break;
                break;
                case "photoDescription";
                $this->photoDescription = $param_value;
                break;
                case "photoUrl";
                $this->photoUrl = $param_value;
                break;
                case "photoLocation";
                $this->photoLocation = $param_value;
                break;
                case "photoLatitude";
                $this->photoLatitude = $param_value;
                break;
                case "photoLongitude";
                $this->photoLongitude = $param_value;
                break;
                case "photoComment";
                $this->photoComment = $param_value;
                break;

                case "cid";
                $this->cid = $param_value;
                break;
            }
        }
    }

    function request($request) {
        switch ($request) {
            case 'category_get_all':
                Category::category_get_all();
                break;
            case 'user_register':
                User::user_register($this->userName, $this->userEmail, $this->userPassword, $this->userPhoto);
                break;
            case 'user_login':
                User::user_login($this->userEmail, $this->userPassword);
                break;
            case 'user_update_status':
                User::user_update_status($this->uid, $this->userStatus, $this->today);
                break;
            case 'photo_get_all':
                Photo::photo_get_all();
                break;
            case 'photo_get_with_uid':
                Photo::photo_get_with_uid($this->uid);
                break;
            case 'photo_get_with_pid':
                Photo::photo_get_with_pid($this->pid);
                break;
            case 'photo_get_with_cid':
                Photo::photo_get_with_cid($this->cid);
                break;
            case 'photo_get_likes_with_pid':
                Photo::photo_get_likes_with_pid($this->pid);
                break;
            case 'photo_get_comments_with_pid':
                Photo::photo_get_comments_with_pid($this->pid);
                break;
            case 'photo_insert_new':
                Photo::photo_insert_new($this->photoName, $this->photoDescription, $this->photoUrl, $this->photoLocation, $this->photoLatitude, $this->photoLongitude, $this->today, $this->uid, $this->cid);
                break;
            case 'photo_insert_new_upload' :
                Photo::check_is_there_any_file_uploaded($_FILES['photoImage']);
                break;
            case 'photo_insert_new_like':
                Photo::photo_insert_new_like($this->today, $this->pid, $this->uid);
                break;
            case 'photo_insert_new_comment':
                Photo::photo_insert_new_comment($this->photoComment, $this->today, $this->pid, $this->uid);
                break;
            case 'photo_delete':
                Photo::photo_delete($this->pid);
                break;
            case 'photo_delete_like':
                Photo::photo_delete_like($this->uid, $this->pid);
                break;
            case 'photo_delete_comment':
                Photo::photo_delete_comment($this->uid, $this->pid);
                break;
        }
    }
}

?>
<?php
class Database{
	public $server = "localhost";
	public $username = "root";
	public $password = "";
	public $database = "photo_hunting";
	
	public $connection;
	
	public function __construct(){
		$this->connection = mysqli_connect($this->server, $this->username, $this->password, $this->database);
		$this->getConnection();
	}
	
	public function getConnection(){
		if ($this->connection === false) {
			return mysqli_connect_error;
		}
		return $this->connection;
	}
}
?>
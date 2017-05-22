<?php 

class CRUD {
    public static $SELECT = 'SELECT';
    public static $INSERT = 'INSERT';
    public static $UPDATE = 'UPDATE';
    public static $DELETE = 'DELETE';

    public static
    function newConnection() {
        $db = new Database;
        $connection = $db->getConnection();
        return $connection;
    }

    public static
    function execute($sql, $type) {
        switch ($type) {
            case CRUD::$SELECT:
                CRUD::select($sql);
                break;
            case CRUD::$INSERT:
                CRUD::insert($sql);
                break;
            case CRUD::$UPDATE:
                CRUD::update($sql);
                break;
            case CRUD::$DELETE:
                CRUD::delete($sql);
                break;
        }
    }

    public static
    function select($sql) {
        $connect = CRUD::newConnection();
        $request = $connect->query($sql);

        if ($request) {
            $result = array();
            if ($request->num_rows > 0) {
                while ($row = $request->fetch_assoc()) {
                    $result[] = $row;
                }
                CRUD::success($result);
            } else {
                CRUD::error("false");
            }
        } else {
            CRUD::error("failed to execute select data ".mysqli_error($connect));
        }
    }

    public static
    function insert($sql) {
        $connect = CRUD::newConnection();
        if ($connect->query($sql) === TRUE) {
            CRUD::success("insert is successfully execute");
        } else {
            CRUD::error("failed to execute insert data ".mysqli_error($connect));
        }
    }

    public static
    function update($sql) {
        $connect = CRUD::newConnection();
        if ($connect->query($sql) === TRUE) {
            CRUD::success("update data is successfully");
        } else {
            CRUD::error($connect->error);
        }

    }

    public static
    function delete($sql) {
        $connect = CRUD::newConnection();
        if ($connect->query($sql) === TRUE) {
            CRUD::success("delete data is successfully");
        } else {
            CRUD::error($this->connection->error);
        }
    }

    public static
    function success($data) {
        $result = array('status' => 200, 'message' => true, 'data' => $data);
        header('Content-Type: application/json');
        echo json_encode($result);
    }

    public static
    function error($data) {
        $result = array('status' => 200, 'message' => false, 'data' => $data);
        header('Content-Type: application/json');
        echo json_encode($result);
    }
}
?>
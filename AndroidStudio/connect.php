<?php
$host = "localhost";
$user = "root";
$pass = "";
$database = "databanhang";

$conn = mysqli_connect($host, $user, $pass, $database);
mysqli_set_charset($conn,"utf8");

if($conn) {
	//echo "ok ket noi";
}

?>
<?php
include "connect.php";
mysqli_select_db($conn, "databanhang");

$id = $_POST['id'];
	
//check data
$query ='DELETE FROM `sanphammoi` WHERE `id`='.$id;
$data = mysqli_query($conn , $query);
if ($data== true) {
	$arr = [
		'success' => true,
		'message' => "thanh cong"
		
	];
}else{
	$arr = [
		'success' => false,
		'message' => " khong thanh cong"
		
	];
}

print_r(json_encode($arr));

?>
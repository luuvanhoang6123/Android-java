<?php
include "connect.php";
mysqli_select_db($conn, "databanhang");
$email = $_POST['email'];
$pass = $_POST['pass'];
$username = $_POST['username'];
$mobile = $_POST['mobile']

	$query = 'INSERT INTO `user`( `email`, `pass`, `username`, `mobile`) VALUES ("'.$email.'","'.$pass.'","'.$username. '","'.$mobile.'")';
	$data = mysqli_query($conn, $query);

	if ($data == true) {	
		$arr = [
		'success' => true,
		'message' => " thanh cong",
		
	];
	}else{
		$arr = [
		'success' => false,
		'message' => " khong thanh cong",
		'result' => $result
	];
}

print_r(json_encode($arr));

?>
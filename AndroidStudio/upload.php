<?php  
include "connect.php";
mysqli_select_db($conn, "databanhang");

$target_dir = "uploads/"; 

$query = "select max(id) as id from sanphammoi";
$data = mysqli_query($conn, $query);
$result = array(); 
while ($row = mysqli_fetch_assoc($data)) {
   $result[]= ($row);
   // code...
}

if ($result[0]['id']==null) {
   $name = 1;
}else{
   $name = ++$result[0]['id'];
}
$name = $name. ".jpg";
$target_file_name = $target_dir .$name;  
  
if (isset($_FILES["file"]))  
   {  
   if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name)  
   )  
      {  
         $arr = [
      'success' => true,
      'message' => "thanh cong",
      'name'=>$name,
   ]; 
      }  
   else  
      {  
         $arr = [
      'success' => false,
      'message' => "khong thanh cong",
      
   ];
      }  
   }  
else  
   {  
      $arr = [
      'success' => false,
      'message' => "khong thanh cong",
      
   ];
   }  
   
   echo json_encode($arr);  
?>
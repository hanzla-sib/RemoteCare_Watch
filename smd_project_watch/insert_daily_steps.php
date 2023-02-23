<?php

include "config.php";
$response=array();
if(isset($_POST["Demail"],$_POST["steps_daily"]))
{
	$email=$_POST["Demail"];
	$steps_daily=$_POST["steps_daily"];

    // echo $email;

	$query="INSERT INTO `daily_steps`( `Demail`, `steps_daily`) VALUES ('$email','$steps_daily')";

	$res=mysqli_query($con,$query);


	if($res)
	{
		$response['id']=mysqli_insert_id($con);
		$response['reqmsg']="daily steps Inserted!";
		$response['reqcode']="1";
	}
	else{
		$response['id']="NA";
		$response['reqmsg']="Error Inserting daily steps!";
		$response['reqcode']="0";

	}
}
else
{
	$response['id']="NA";
	$response['reqmsg']="Incomplete Request!";
	$response['reqcode']="0";
}

// $x=json_encode($response);
// echo $x;



?>

<?php

include "config.php";
$response=array();

if(isset($_POST["email"],$_POST["h_rate"]))
{
	$date = new DateTime("now", new DateTimeZone('Asia/Karachi') );
	$date=$date->format('Y-m-d');
	
	$HR=$_POST["h_rate"];
	$email=$_POST["email"];
	
	$sql = "SELECT * FROM `daily_steps` where `Demail`= '$email' and `date_log`='$date' ";
	$result = $con->query($sql);
	if ($result->num_rows > 0) {
		$query="UPDATE `daily_steps` SET `heartbeat`='$HR' where `Demail`='$email'";
		$res=mysqli_query($con,$query);
		if($res){
			echo "HR updated";
		}
		else{
			echo "HR not updated";	
		}
	}
	else{
		$query="UPDATE `daily_steps` SET `heartbeat`='0',`date_log`='$date' where `Demail`='$email'";
		$res=mysqli_query($con,$query);
		if($res){
			echo "HR updated";
		}
		else{
			echo "HR not updated";	
		}
	}

	


	
		$query="INSERT INTO `heartbeat`(`Demail`, `HeartBeat_daily`, `date_log` ) VALUES ('$email','$HR','$date')";
		$res=mysqli_query($con,$query);
		if($res){
			echo "insereted in heartbeatweekly";
		}
		else{
			echo "not inserted error";
		}
	




}
else
{
	$response['email']="NA";
	$response['reqmsg']="Incomplete Request!";
	$response['reqcode']="0";
	$x=json_encode($response);
	echo $x;
}

?>

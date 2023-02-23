<?php

include "config.php";
$response=array();

if(isset($_POST["email"],$_POST["steps"]))
{
	$date = new DateTime("now", new DateTimeZone('Asia/Karachi') );
	$date=$date->format('Y-m-d');
	$calories_burnt=$_POST["calories_burn"];
	$steps=$_POST["steps"];
	$email=$_POST["email"];
	$motion=$_POST["Motion"];
	$sql = "SELECT * FROM `daily_steps` where `Demail`= '$email' and `date_log`='$date' ";
	$result = $con->query($sql);
	if ($result->num_rows > 0) {
		$query="UPDATE `daily_steps` SET `steps_daily`='$steps',`motion`='$motion',`Burnt_Calories`='$calories_burnt' where `Demail`='$email'";
		$res=mysqli_query($con,$query);
		if($res){
			echo "Steps updated";
		}
		else{
			echo "Steps not updated";	
		}
	}
	else{
		$query="UPDATE `daily_steps` SET `steps_daily`='0',`motion`='$motion',`date_log`='$date' where `Demail`='$email'";
		$res=mysqli_query($con,$query);
		if($res){
			echo "Steps updated";
		}
		else{
			echo "Steps not updated";	
		}
	}

	

	$sql = "SELECT * FROM `weekly_steps` where `Demail`= '$email' and `date_log`='$date' ";
	$result = $con->query($sql);
	if ($result->num_rows > 0) {
		$query="UPDATE `weekly_steps` SET `steps_daily`='$steps',`Burnt_Calories`='$calories_burnt' where `Demail`='$email' and `date_log`='$date'";
		$res=mysqli_query($con,$query);
		if($res){
			echo "Steps updated";
		}
		else{
			echo "Steps not updated";	
		}
	}
	else{
		$query="INSERT INTO `weekly_steps`(`Demail`, `date_log`, `steps_daily` ) VALUES ('$email','$date','$steps')";
		$res=mysqli_query($con,$query);
		if($res){
			echo "insereted in Weeklytable";
		}
		else{
			echo "not inserted error";
		}
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

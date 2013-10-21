<?php
mysql_connect("##DATABASE ADDRESS##","##LOGIN##","##PASSWORD##");
mysql_select_db("##DATABASE_NAME##");

$RKID = $_POST['RKID'];
$Dist = (float)$_POST['Distance'];
$CheckExisting = "SELECT COUNT(*) FROM wnc_user_miles WHERE RKID = '$RKID'";
$result = mysql_query($CheckExisting);
$q=mysql_query("INSERT INTO wnc_user_miles (RKID, Distance, RedemDate) VALUES ('$RKID', '$Dist',NULL)");
if(mysql_result($result,0) > 0)
{
	$q=mysql_query("Update wnc_user_miles set Distance = Distance + '$Dist' where RKID = '$RKID'");
}
else
{
	$q=mysql_query("INSERT INTO wnc_user_miles (RKID, Distance, RedemDate) VALUES ('$RKID', '$Dist',NULL)");
}  
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>
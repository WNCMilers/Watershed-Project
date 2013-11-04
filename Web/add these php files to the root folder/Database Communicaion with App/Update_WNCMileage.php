<?php
mysql_connect("HOST","USER","PASSWORD");
mysql_select_db("DATABASE");

$RKID = $_POST['RKID'];
$Dist = (float)$_POST['Distance'];
$WorkoutType = (float)$_POST['WorkoutType'];
$q=mysql_query("INSERT INTO workouts (RK_ID, Distance, WorkoutType) VALUES ('$RKID', '$Dist','$WorkoutType')");
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>
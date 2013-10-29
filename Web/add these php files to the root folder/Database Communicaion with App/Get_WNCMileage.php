<?php 

      mysql_connect("HOST","USERNAME","PASSWORD");

      mysql_select_db("DATABASE");
	  $RKID = $_POST['RKID']; 
      $q=mysql_query("SELECT sum(Distance) as Distance, Redemption_Date FROM workouts left join membership_redeemed on RK_ID = Membership_RK_ID Where RK_ID = '$RKID' group by RK_ID");

      while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));
     
    mysql_close();
?>
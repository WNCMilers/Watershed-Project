<?php 

      mysql_connect("##HOST##","##USER##","##Password##");

      mysql_select_db("##Database##");
	  $RKID = $_POST['RKID']; 
      $q=mysql_query("SELECT Miles, RedemDate FROM wnc_user_miles Where RKID = '$RKID'");

      while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));
     
    mysql_close();
?>
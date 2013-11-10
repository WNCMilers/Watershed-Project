<?php
mysql_connect("HOST","USERNAME","PASSWORD");
mysql_select_db("Database");

//prevent sql injection:
$RKID = mysql_real_escape_string($RKID);
$designation = mysql_real_escape_string($designation);
$firstname = mysql_real_escape_string($firstname);
$lastname = mysql_real_escape_string($lastname);
$address1 = mysql_real_escape_string($address1);
$address2 = mysql_real_escape_string($address2);
$city = mysql_real_escape_string($city);
$state = mysql_real_escape_string($state);
$zip = mysql_real_escape_string($zip);
$phone = mysql_real_escape_string($phone);
$email = mysql_real_escape_string($email);
$birthdate = mysql_real_escape_string($birthdate);
$sqlBirthdate = date('Y-m-d', strtotime($birthdate));
$end= date('Y-m-d', strtotime('+1 year'));
$membershiplevel = mysql_real_escape_string($membershiplevel);
//$emailEnd = date('m-d-Y', strtotime($end));

$RKID = $_POST['RKID'];
$designation = $_POST['Title'];
$firstname = $_POST['FirstName'];
$lastname = $_POST['LastName'];
$address1 = $_POST['AddressLine1'];
$address2 = $_POST['AddressLine2'];
$city = $_POST['City'];
$state = $_POST['State'];
$zip = $_POST['Zip'];
$phone = $_POST['Phone'];
$email = $_POST['EmailAddress'];
$sqlBirthdate = $_POST['BirthDate'];
$membershiplevel = $_POST['membershiplevel'];
//$end= $_POST['MembershipExpiration'];

$q=mysql_query("INSERT INTO users (RunkeeperID, Title, FirstName, LastName, AddressLine1, AddressLine2, City, State, Zip, EmailAddress, BirthDate, Phone, MembershipExpiration, MembershipType) VALUES ('$RKID','$designation', '$firstname', '$lastname', '$address1', '$address2', '$city', '$state', '$zip', '$email', '$sqlBirthdate', '$phone', '$end', '$membershiplevel')") or die (mysql_error());
$q=mysql_query("INSERT INTO membership_redeemed (Membership_RK_ID) VALUES ('$RKID')") or die (mysql_error());

while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>
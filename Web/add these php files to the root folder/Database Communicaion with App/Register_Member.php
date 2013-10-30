<?php
mysql_connect("HOST","USERNAME","PASSWORD");
mysql_select_db("Database");

//$designation = mysql_real_escape_string($designation);
//$firstname = mysql_real_escape_string($firstname);
//$lastname = mysql_real_escape_string($lastname);
//$address1 = mysql_real_escape_string($address1);
//$address2 = mysql_real_escape_string($address2);
//$city = mysql_real_escape_string($city);
//$state = mysql_real_escape_string($state);
//$zip = mysql_real_escape_string($zip);
//$phone = mysql_real_escape_string($phone);
//$email = mysql_real_escape_string($email);
//$birthdate = mysql_real_escape_string($birthdate);
//$sqlBirthdate = date('Y-m-d', strtotime($birthdate));
$end= date('Y-m-d', strtotime('+1 year'));
//$emailEnd = date('m-d-Y', strtotime($end));

$firstname = $_POST['FirstName'];
$lastname = $_POST['LastName'];
$address1 = $_POST['AddressLine1'];
$address2 = $_POST['AddressLine2'];
$city = $_POST['City'];
$state = $_POST['State'];
$zip = $_POST['Zip'];
$phone = $_POST['Phone'];
$email = $_POST['EmailAddress'];
//$sqlBirthdate = $_POST['BirthDate'];
//$end= $_POST['MembershipExpiration'];

$q=mysql_query("INSERT INTO users (FirstName, LastName, AddressLine1, AddressLine2, City, State, Zip, EmailAddress, Phone, MembershipExpiration) VALUES ('$firstname', '$lastname', '$address1', '$address2', '$city', '$state', '$zip', '$email', '$phone', '$end')");
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>
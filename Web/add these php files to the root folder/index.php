<?php $title="Membership" ?>
<?php include("#top.htm");?>

<!-- begin blog content -->
<section id="blog" class="container_12 clearfix">

	<!-- left column -->
	<div class="grid_8">

		<!-- featured post -->
		<div class="post featured">
			<!-- headings -->

			<!-- / headings -->

			<!-- featured post full image - link -->
			<figure class="full">
				<a href="blog-post.htm">
				</a>
			</figure>
			<!-- / featured post full image - link -->

			<!-- featured post preview text -->
		<br>  
			<br>
			

              <div style="clear: both; height: 5px;">
              </div>
          </div>
    
			<!-- featured post preview text -->

		</div>
	
			<!-- / post preview -->
		</div>
	
		<!-- / info about post -->

		<hr />

		
		<!-- / posts pager -->

	</div>
	<!-- / left column -->
<!-- <div class="error">Some required fields are missing.  They are indicated in red.  Please fill out these fields, and submit the form again.</div>       -->
<html>
<head>
<style>
.error{color: #FF0000;}
</style>
</head>
<?php
//Debug mode:
error_reporting(E_ALL);
// define variables and set to empty values
$firstnameErr = $lastnameErr = $genderErr = $address1Err = $address2Err = $cityErr = $stateErr = $zipErr = $emailErr  = $phoneErr = $emailErr = $birthdateErr = $membershipErr = "";
$firstname= $lastname = $gender = $address1 = $address2 = $city = $state = $zip  = $phone = $email = $birthdate = $membership = "";
//Sure you want to show some error if smth went wrong:
$errors = array(); 

if ($_SERVER["REQUEST_METHOD"] == "POST")
{
    //title
   #if (empty($_POST["designation"]))
     #{$designationErr = "Title is required";}
   #else
     #{$designation = test_input($_POST["designation"]);}
     
    //first name
   if (empty($_POST["firstname"]))
     {
       $firstnameErr = "First Name is required";
       $errors[] = $firstnameErr;
     }
   else
     {
     $firstname = test_input($_POST["firstname"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^[a-zA-Z ]*$/",$firstname))
       {
       $firstnameErr = "Only letters and white space allowed"; 
       $errors[] = $firstnameErr;
       }
     }
   //last name 
   if (empty($_POST["lastname"]))
     {
       $lastnameErr = "Last Name is required";
       $errors[] = $lastnameErr;
     }
   else
     {
     $lastname = test_input($_POST["lastname"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^[a-zA-Z ]*$/",$lastname))
       {
       $lastnameErr = "Only letters and white space allowed";
       $errors[] = $lastnameErr;
       }
     }
   //gender
   if (empty($_POST["gender"]))
     {
       $genderErr = "Gender is required";
       $errors[] = $genderErr;
     }
   else
     {$gender = test_input($_POST["gender"]);}
     
   //address1  
   if (empty($_POST["address1"]))
     {
        $address1Err = "Address is required";
        $errors[] = $address1Err;
     }
   else
     {
     $address1 = test_input($_POST["address1"]);
     $errors[] = $address1Err;
     // check if address address syntax is valid
    # if (!preg_match("/^[a-zA-Z0-9,]+$/",$address1))
       #{
       #$address1Err = "Invalid address format"; 
       #}
     }
     
   //address2
   if (empty($_POST["address2"]))
     {$address2Err = "";}
   else
     {
     $address2 = test_input($_POST["address2"]);
     // check if address address syntax is valid
     #if (!preg_match("/^[a-zA-Z0-9,]+$/",$address2))
       #{
       #$address2Err = "Invalid address format"; 
       #}
     }
     
   //city 
   if (empty($_POST["city"]))
     {
       $cityErr = "City is required";
       $errors[] = $cityErr;
     }
   else
     {
     $city = test_input($_POST["city"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^(?:[a-zA-Z]+(?:[.'\\-,])?\\s?)+$/",$city))
      {
        $cityErr = "Only letters and white space allowed - Please mark any punctuation as a space"; 
        $errors[] = $cityErr;
      }
     }
   
   //state
   if (empty($_POST["state"]))
     {
       $stateErr = "State is required";
       $errors[] = $stateErr;
     }
   else
     {$state = test_input($_POST["state"]);}
     
   //zip 
   if (empty($_POST["zip"]))
     {
       $zipErr = "Zip is required";
       $errors[] = $zipErr;
     }
   else
     {
     $zip = test_input($_POST["zip"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^[0-9]{5}$/", $zip))
       {
         $zipErr = "Please enter your 5 digit zip code";
         $errors[] = $zipErr;
       }
     }
     
   //homephone 
   if (empty($_POST["phone"]))
     {
       $phoneErr = "Preferred phone number is required";
       $errors[] = $phoneErr;
     }
   else
     {
     $phone = test_input($_POST["phone"]);
     // check if homephone number only contains 10 digits
     if (!preg_match("/^\\d{3}-\\d{3}-\\d{4}$/", $phone))
       {
         $phoneErr = "Please enter your 10 digit phone number in the format below"; 
         $errors[] = $phoneErr;
       }
     }
    
     
   if (empty($_POST["email"]))
     {
       $emailErr = "Email is required";
       $errors[] = $emailErr;
     }
   else
     {
     $email = test_input($_POST["email"]);
     // check if e-mail address syntax is valid
     if (!preg_match("/([\w\-]+\@[\w\-]+\.[\w\-]+)/",$email))
       {
         $emailErr = "Invalid email format"; 
         $errors[] = $emailErr;
       }
     }  
     
     if (empty($_POST["birthdate"]))
     {
         $birthdateErr = "Birthdate is required";
         $errors[] = $birthdateErr;
     }
     else
     {
     $birthdate = test_input($_POST["birthdate"]);
     // check if birthdate address syntax is valid
     if (!preg_match("@^[0-9]{2}/[0-9]{2}/[0-9]{4}$@",$birthdate))
       {
          $birthdateErr = "Please enter your birthdate in the format below";
          $errors[] = $birthdateErr;
       }
     }
     //gender
     if (empty($_POST["membership"]))
      {
         $membershipErr = "Membership level is required";
         $errors[] = $membershipErr;
      }
     else
      {$membership = test_input($_POST["membership"]);}
      
      
       //Connect sql server:
    if ( !connect() )
    {
        $connected = false;
        $errors[] = "Can't establish link to MySQL server";
    }
    else
    {
        $connected = true;
    }

   // if ( empty($errors) ){
    if($firstnameErr == "" && $lastnameErr == "" && $genderErr == "" && $address1Err == "" && $address2Err == "" && $cityErr == "" && $stateErr == ""  && $zipErr == "" && $phoneErr == "" && $emailErr == "" && $birthdateErr == "" && $membershipErr == "" && $connected == true){
        //let's prevent sql injection:

        $firstname = mysql_real_escape_string($firstname);
        $lastname = mysql_real_escape_string($lastname);
        $gender = mysql_real_escape_string($gender);
        $address1 = mysql_real_escape_string($address1);
        $address2 = mysql_real_escape_string($address2);
        $city = mysql_real_escape_string($city);
        $state = mysql_real_escape_string($state);
        $zip  = mysql_real_escape_string($zip);
        $phone = mysql_real_escape_string($phone);
        $email = mysql_real_escape_string($email);
        $birthdate = mysql_real_escape_string($birthdate);
        $membership = mysql_real_escape_string($membership);
        $errors[] = "sql injection prevented\n\r";
        //Please do this for all of them..
        $query = "INSERT INTO `users` (`FirstName`,`LastName`,`EmailAddress`,`Phone`) VALUES ('$firstname', '$lastname', '$email', '$phone')";
        //$query2 = "INSERT INTO 'useraddress' ('Address','City','State','Zip') VALUES ('$address1', '$city','$state', '$zip')";
        //So try it:
        if ( !mysql_query($query) ){
           // 
           //die (mysql_error());
           $errors[] = "Can't insert the vals \n\r";
        } else {
           //Or on success:
           //$link = "<script>window.open('/membershipReg/thanks.php</script>";
           //echo $link;
           //$errors[] = "Successfully added to database!";
           
            
            //if ( mysql_query($query2) ){
            //    print("Address submitted");
            //}
            
            //$ExpireDate = Date("l F d, Y+1");
            $end = date('F d, Y', strtotime('+1 year'));
            $autoreply="Thank you for becoming a member, $firstname! 
                \n\nYour membership with the Watershed Nature Center is valid thru $end.
                \n\n\n\n\n\nPlease do not reply to this email.";
            #$subject="Thank you for becoming a member";
            //mail($email, $subject, $autoreply, 'From: no-reply@watershednaturecenter.com');
            
            $ToEmail = $email;
            //$ToName  = 'Name';

            $Send = SendMail( $ToEmail, $autoreply );
            if ( $Send ) {
              header( 'Location: http://localhost/membershipReg/thanks.php' );
            }
            else {
              echo "<h2> ERROR</h2>";
            }
            
            
            //print ("Thank you for your registration, $firstname! \n\nYou should receive an email at the following address provided: $email");
            //header( 'Location: http://localhost/membershipReg/thanks.php' );
          
        }
    }
 
}

function SendMail( $ToEmail, $MessageTEXT ) 
{
  #require_once ( 'class.phpmailer.php' ); // Add the path as appropriate
   include_once('class.phpmailer.php');
   require_once('class.smtp.php');
  $Mail = new PHPMailer();
  $Mail->IsSMTP(); // Use SMTP
  $Mail->Host        = "smtp.gmail.com"; // Sets SMTP server
  $Mail->SMTPDebug   = 2; // 2 to enable SMTP debug information
  $Mail->SMTPAuth    = TRUE; // enable SMTP authentication
  $Mail->SMTPSecure  = "ssl";//"tls"; //Secure conection
  $Mail->Port        = 465;//587; // set the SMTP port
  $Mail->Username    = 'slkatzer@gmail.com'; // SMTP account username
  $Mail->Password    = 'slugssuck2'; // SMTP account password
  $Mail->Priority    = 1; // Highest priority - Email priority (1 = High, 3 = Normal, 5 = low)
  $Mail->CharSet     = 'UTF-8';
  $Mail->Encoding    = '8bit';
  $Mail->Subject     = 'Thank You for Becoming a Member';
  $Mail->ContentType = 'text/plain; charset=utf-8\r\n';
  $Mail->From        = 'MyGmail@gmail.com';
  $Mail->FromName    = 'Watershed Nature Center';
  $Mail->WordWrap    = 900; // RFC 2822 Compliant for Max 998 characters per line

  $Mail->AddAddress( $ToEmail ); // To:
  //$Mail->isHTML( TRUE );
  //$Mail->Body    = $MessageHTML;
  $Mail->Body = $MessageTEXT;
  $Mail->Send();
  $Mail->SmtpClose();

  if ( $Mail->IsError() ) { // ADDED - This error checking was missing
    return FALSE;
  }
  else {
    return TRUE;
  }
}




function test_input($data)
{
     $data = trim($data);
     $data = stripslashes($data);
     $data = htmlspecialchars($data);
     return $data;
}
function connect(){

 $connection = mysql_connect("97.92.225.112:3306", "samk", "jagerbombs4589");
 $db = mysql_select_db("wnc",$connection);    

 if (!$connection || !$db ){
   return false; 
 } else {
   return true;
 }
}
?>
<script type="text/javascript">
		
    function clearForm() {
            $('input[type="text"]').val("");
            //$('#schedule_designation').val("");
            $('#schedule_sex').val("");
            $('#schedule_state'.val(""));

    }

    $("#showForm").live('click', function() {
            $("#showFormP").hide();
            $("#formDiv").slideDown();
    });
</script>

<div id="formDiv" style="display: block;">
    <h2>Membership Registration</h2>
    <p><span class="error">* </span> required field.</p>
        <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> <!-- action="index2.php" -->
            <table width="50%">
                <col width="50">
                <col width="200">
                <tr><td><!-- Spacer --><img src="/Images/empty.png" width="170" height="2" /></td><td></td></tr>
                    <tr>
                        <td class='boldText'>First Name</td><td><input type='text' id='schedule_firstname' name='firstname' value="<?php echo $firstname;?>"> <span class="error">* <?php echo $firstnameErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Last Name</td><td><input type='text' id='schedule_lastname' name='lastname' value="<?php echo $lastname;?>"> <span class="error">* <?php echo $lastnameErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Gender</td><td>
                            <select id="gender" name="gender">
                                
                            <option value="" <?php if(isset($_POST['gender']) && $_POST['gender'] == '') echo ' selected="selected"';?>>Click To Select</option>
                            <option value="M" <?php if(isset($_POST['gender']) && $_POST['gender'] == 'M') echo ' selected="selected"';?>>Male</option>
                            <option value="F" <?php if(isset($_POST['gender']) && $_POST['gender'] == 'F') echo ' selected="selected"';?>>Female</option>
                            
                            <!--<option value=""></option>
                            <option value="M">Male</option>
                            <option value="F">Female</option>-->
                        </select><span class="error"> * <?php echo $genderErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Address 1</td><td><input type='text' id='schedule_address' name="address1" value="<?php echo $address1;?>"> <span class="error">* <?php echo $address1Err;?></span></td>
                    </tr>
                    <tr>
                        <td >Address 2</td><td><input type='text' id='schedule_address2' name="address2" value="<?php echo $address2;?>"> <span class="error"> <?php echo $address2Err;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>City</td><td><input type='text' id='city' name="city" value="<?php echo $city;?>"> <span class="error">* <?php echo $cityErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>State</td><td><select id='state' name='state'>
                                 <option value="" <?php if(isset($_POST['state']) && $_POST['state'] == '') echo ' selected="selected"';?>>Click To Select</option>
                                 <option value="AL" <?php if(isset($_POST['state']) && $_POST['state'] == 'AL') echo ' selected="selected"';?>>Alabama</option>
                                 <option value="AK" <?php if(isset($_POST['state']) && $_POST['state'] == 'AK') echo ' selected="selected"';?>>Alaska</option>
                                 <option value="AZ" <?php if(isset($_POST['state']) && $_POST['state'] == 'AZ') echo ' selected="selected"';?>>Arizona</option>
                                 <option value="AR" <?php if(isset($_POST['state']) && $_POST['state'] == 'AR') echo ' selected="selected"';?>>Arkansas</option>
                                 <option value="CA" <?php if(isset($_POST['state']) && $_POST['state'] == 'CA') echo ' selected="selected"';?>>California</option>
                                 <option value="CO" <?php if(isset($_POST['state']) && $_POST['state'] == 'CO') echo ' selected="selected"';?>>Colorado</option>
                                 <option value="CT" <?php if(isset($_POST['state']) && $_POST['state'] == 'CT') echo ' selected="selected"';?>>Connecticut</option>
                                 <option value="DE" <?php if(isset($_POST['state']) && $_POST['state'] == 'DE') echo ' selected="selected"';?>>Delaware</option>
                                 <option value="DC" <?php if(isset($_POST['state']) && $_POST['state'] == 'DC') echo ' selected="selected"';?>>District Of Columbia</option>
                                 <option value="FL" <?php if(isset($_POST['state']) && $_POST['state'] == 'FL') echo ' selected="selected"';?>>Florida</option>
                                 <option value="GA" <?php if(isset($_POST['state']) && $_POST['state'] == 'GA') echo ' selected="selected"';?>>Georgia</option>
                                 <option value="HI" <?php if(isset($_POST['state']) && $_POST['state'] == 'HI') echo ' selected="selected"';?>>Hawaii</option>
                                 <option value="ID" <?php if(isset($_POST['state']) && $_POST['state'] == 'ID') echo ' selected="selected"';?>>Idaho</option>
                                 <option value="IL" <?php if(isset($_POST['state']) && $_POST['state'] == 'IL') echo ' selected="selected"';?>>Illinois</option>
                                 <option value="IN" <?php if(isset($_POST['state']) && $_POST['state'] == 'IN') echo ' selected="selected"';?>>Indiana</option>
                                 <option value="IA" <?php if(isset($_POST['state']) && $_POST['state'] == 'IA') echo ' selected="selected"';?>>Iowa</option>
                                 <option value="KS" <?php if(isset($_POST['state']) && $_POST['state'] == 'KS') echo ' selected="selected"';?>>Kansas</option>
                                 <option value="KY" <?php if(isset($_POST['state']) && $_POST['state'] == 'KY') echo ' selected="selected"';?>>Kentucky</option>
                                 <option value="LA" <?php if(isset($_POST['state']) && $_POST['state'] == 'LA') echo ' selected="selected"';?>>Louisiana</option>
                                 <option value="ME" <?php if(isset($_POST['state']) && $_POST['state'] == 'ME') echo ' selected="selected"';?>>Maine</option>
                                 <option value="MD" <?php if(isset($_POST['state']) && $_POST['state'] == 'MD') echo ' selected="selected"';?>>Maryland</option>
                                 <option value="MA" <?php if(isset($_POST['state']) && $_POST['state'] == 'MA') echo ' selected="selected"';?>>Massachusetts</option>
                                 <option value="MI" <?php if(isset($_POST['state']) && $_POST['state'] == 'MI') echo ' selected="selected"';?>>Michigan</option>
                                 <option value="MN" <?php if(isset($_POST['state']) && $_POST['state'] == 'MN') echo ' selected="selected"';?>>Minnesota</option>
                                 <option value="MS" <?php if(isset($_POST['state']) && $_POST['state'] == 'MS') echo ' selected="selected"';?>>Mississippi</option>
                                 <option value="MO" <?php if(isset($_POST['state']) && $_POST['state'] == 'MO') echo ' selected="selected"';?>>Missouri</option>
                                 <option value="MT" <?php if(isset($_POST['state']) && $_POST['state'] == 'MT') echo ' selected="selected"';?>>Montana</option>
                                 <option value="NE" <?php if(isset($_POST['state']) && $_POST['state'] == 'NE') echo ' selected="selected"';?>>Nebraska</option>
                                 <option value="NV" <?php if(isset($_POST['state']) && $_POST['state'] == 'NV') echo ' selected="selected"';?>>Nevada</option>
                                 <option value="NH" <?php if(isset($_POST['state']) && $_POST['state'] == 'NH') echo ' selected="selected"';?>>New Hampshire</option>
                                 <option value="NJ" <?php if(isset($_POST['state']) && $_POST['state'] == 'NJ') echo ' selected="selected"';?>>New Jersey</option>
                                 <option value="NM" <?php if(isset($_POST['state']) && $_POST['state'] == 'NM') echo ' selected="selected"';?>>New Mexico</option>
                                 <option value="NY" <?php if(isset($_POST['state']) && $_POST['state'] == 'NY') echo ' selected="selected"';?>>New York</option>
                                 <option value="NC" <?php if(isset($_POST['state']) && $_POST['state'] == 'NC') echo ' selected="selected"';?>>North Carolina</option>
                                 <option value="ND" <?php if(isset($_POST['state']) && $_POST['state'] == 'ND') echo ' selected="selected"';?>>North Dakota</option>
                                 <option value="OH" <?php if(isset($_POST['state']) && $_POST['state'] == 'OH') echo ' selected="selected"';?>>Ohio</option>
                                 <option value="OK" <?php if(isset($_POST['state']) && $_POST['state'] == 'OK') echo ' selected="selected"';?>>Oklahoma</option>
                                 <option value="OR" <?php if(isset($_POST['state']) && $_POST['state'] == 'OR') echo ' selected="selected"';?>>Oregon</option>
                                 <option value="PA" <?php if(isset($_POST['state']) && $_POST['state'] == 'PA') echo ' selected="selected"';?>>Pennsylvania</option>
                                 <option value="RI" <?php if(isset($_POST['state']) && $_POST['state'] == 'RI') echo ' selected="selected"';?>>Rhode Island</option>
                                 <option value="SC" <?php if(isset($_POST['state']) && $_POST['state'] == 'SC') echo ' selected="selected"';?>>South Carolina</option>
                                 <option value="SD" <?php if(isset($_POST['state']) && $_POST['state'] == 'SD') echo ' selected="selected"';?>>South Dakota</option>
                                 <option value="TN" <?php if(isset($_POST['state']) && $_POST['state'] == 'TN') echo ' selected="selected"';?>>Tennessee</option>
                                 <option value="TX" <?php if(isset($_POST['state']) && $_POST['state'] == 'TX') echo ' selected="selected"';?>>Texas</option>
                                 <option value="UT" <?php if(isset($_POST['state']) && $_POST['state'] == 'UT') echo ' selected="selected"';?>>Utah</option>
                                 <option value="VT" <?php if(isset($_POST['state']) && $_POST['state'] == 'VT') echo ' selected="selected"';?>>Vermont</option>
                                 <option value="VA" <?php if(isset($_POST['state']) && $_POST['state'] == 'VA') echo ' selected="selected"';?>>Virginia</option>
                                 <option value="VA" <?php if(isset($_POST['state']) && $_POST['state'] == 'VA') echo ' selected="selected"';?>>Washington</option>
                                 <option value="WV" <?php if(isset($_POST['state']) && $_POST['state'] == 'WV') echo ' selected="selected"';?>>West Virginia</option>
                                 <option value="WI" <?php if(isset($_POST['state']) && $_POST['state'] == 'WI') echo ' selected="selected"';?>>Wisconsin</option>
                                 <option value="WY" <?php if(isset($_POST['state']) && $_POST['state'] == 'WY') echo ' selected="selected"';?>>Wyoming</option>
                            </select><span class="error">* <?php echo $stateErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Zip</td><td><input type='text' id='schedule_zip' name="zip" value="<?php echo $zip;?>"> <span class="error">* <?php echo $zipErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Preferred Phone</td><td><input type='text' id='schedule_homephone' name="phone" value="<?php echo $phone;?>"> <span class="error">* <?php echo $phoneErr;?></span><br>
                            <span class='smallInfo'>Please use XXX-XXX-XXXX format, eg. 555-555-5555</span></td>
                        </td>
                    </tr>

                    <tr>
                        <td class='boldText'>E-mail Address</td><td><input type='text' id='schedule_email' name="email" value="<?php echo $email;?>"> <span class="error">* <?php echo $emailErr;?></span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Date of Birth</td><td><input type='text' id='schedule_birthday' name='birthdate' value="<?php echo $birthdate;?>"> <span class="error">* <?php echo $birthdateErr;?></span> <br>
                            <span class='smallInfo'>Please use MM/DD/YYYY format, eg. 12/30/1965</span></td>
                    </tr>
                    <tr>
                        <td class='boldText'>Membership Level</td><td>
                            <select id="membership" name="membership">
                            <option value="" <?php if(isset($_POST['membership']) && $_POST['membership'] == '') echo ' selected="selected"';?>>Click To Select</option>    
                            <option value="scholastic" <?php if(isset($_POST['membership']) && $_POST['membership'] == 'scholastic') echo ' selected="selected"';?>>Scholastic $20.00 USD</option>
                            <option value="individual" <?php if(isset($_POST['membership']) && $_POST['membership'] == 'individual') echo ' selected="selected"';?>>Individual $25.00 USD</option>
                            <option value="family" <?php if(isset($_POST['membership']) && $_POST['membership'] == 'family') echo ' selected="selected"';?>>Family $50.00 USD</option>
                        </select><span class="error"> * <?php echo $membershipErr;?></span></td>
                    </tr>
            </table>
                <!-- Spacer -->
                <div>&nbsp;</div>
                <div style="display: inline">
                <!--<input type="button" value="Clear" onclick="clearForm();"/>-->
                <input type="submit" name='Submit' value="Submit"/>
                </div>
                
			
			<!-- Spacer -->
        </form>
    </div>

<br></br>
<br></br>
<br></br>
<?php 
   foreach($errors as $value) {
       print $value;
       print "\r\n";
    }
 ?>
<?php #phpinfo() ?>
<br></br>
<H4><b>Or, print and mail the application</b></H4>
            <p><a href="WNC_MembershipForm_8.5x11.pdf" target="pdf">Downloadable Membership Application.</a><br>
	        <p>Please mail your application to:</p>
            <p>Watershed Nature Center<br />P.O. Box 843<br />Edwardsville, IL 62025</p>
            <p>Please make checks payable to:<br /> <b>Nature Preserve Foundation</b></p>


		
	</aside>
	<!-- / sidebar -->

</section>
<!-- / blog content -->
<br></br> <br></br> <br></br>



<!--<form name='test' method='POST' action='' accept-charset='UTF-8'>
Name: <input type='text' name='Name' size='20'>
Email: <input type='text' name='Email' size='20'>
<input type='submit' name='Submit' value='Submit'>
</form> -->

<?php include("#bottom.htm"); ?>

<?php
/* Step 4.1 Start */
$db_server = "sophia.cs.hku.hk";
$db_user = "------";
$db_pwd = "------";
$link = mysqli_connect($db_server, $db_user, $db_pwd, $db_user) or die(mysqli_error());
/* Step 4.1 End */

/* Step 5.1 Start */
$action = (isset($_GET['action']) ? $_GET['action'] : "");
$username = (isset($_GET['username']) ? $_GET['username'] : "");
$email = (isset($_GET['email']) ? $_GET['email'] : "");
$password = (isset($_GET['password']) ? $_GET['password'] : "");
if ($action == "insert" && $username && $email && $password) {
  $sql = "INSERT INTO cognizance (username, email, password) VALUES ('$username', '$email', '$password');";
  $res = mysqli_query($link, $sql) or die(mysqli_error());
  $student_id = mysqli_insert_id();
}
/* Step 5.1 End */

$cognizance = array();
$sql = "SELECT username,email,password FROM cognizance";
$res = mysqli_query($link, $sql) or die(mysqli_error());
mysqli_close($link);
while ($row = mysqli_fetch_array($res)) {
	array_push($cognizance, $row['username']);
	array_push($cognizance, $row['email']);
	array_push($cognizance, $row['password']);
}
echo '{';
echo '"cognizance":[';
$add_delimiter = false;
for ($i=0; $i<count($cognizance); $i++) {
  echo ($add_delimiter ? ', ' : '') . '"' . $cognizance[$i] . '"';
  $add_delimiter = true;
  }
echo ']';

echo '}';
?>

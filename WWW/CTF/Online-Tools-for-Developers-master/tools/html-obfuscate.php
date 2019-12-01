<h2>HTML Obfuscate</h2>
<?php
$html = "";
$html_obfuscated = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  if (empty($_POST["html"])) {
    $textErr = "You must enter text";
  } else {
    $html = test_input($_POST["html"]);
  }
}
function test_input($data) {
  $data = trim($data);
  $data = stripslashes($data);
  return $data;
}
?>
<p><b>Enter HTML :</b></p>
<form class="FormManagertools" method="post" accept-charset="utf-8" action="<?php echo htmlspecialchars($_SERVER["REQUEST_URI"]);?>#result">
<ul>
<li>
<textarea name="html" class="field-style"><?php echo htmlspecialchars($html);?></textarea>
</li>
<li>
<input type="submit" name="submit" value="Convert" />
</li>
</ul>
</form>
<?php
if ( isset($textErr) ) {
echo '<div id="result">';
echo '<span class="error">* '.$textErr.'</span><br>';
echo '</div>';
}
echo "<p><b>HTML Obfuscated :</b></p>";

if ($html <> ""){
  $html_obfuscated = "<script language=JavaScript> html='".rawurlencode($html)."';d=unescape(html);document.write(d);</script>";  
 }

echo '<div id="result">';
echo '<form class="FormManagertools"><ul><li>';
echo '<textarea name="result" class="field-style">'.$html_obfuscated.'</textarea></li></ul></form>';
echo '</div>';
?>
<p>&nbsp;</p>

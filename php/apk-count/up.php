<?php require 'data.php' ?>
<?php
if (isset($_POST['key']) && isset($_POST['apk'])){
    $key = $_POST['key'];
    $apk = $_POST['apk'];
    if($key == 'xuan15858178400'){
	    if(!isset($apk_count[$apk])){
		    //新增apk
			$apk_count[$apk] = 1;
		}else{
		    //原有apk添加值
			$count = $apk_count[$apk];
			$count = $count + 1;
			$apk_count[$apk] = $count;
		}
		
		$code = "<?php\n\$apk_count = ".var_export($apk_count, true)."\n?>";
		file_put_contents('data.php', $code);
		echo '1';
    }else{
        echo '0';
    }
}else{
    echo '0';
}
?>
<?php require_once 'adminAction.php'?>

<html>
<head><title>数据库管理</title></head>
<body>

<h3>数据库表：</h3>
<table border="1">
	<tr>
		<td>序号</td>
		<td>数据库表名</td>
		<td>操作</td>
	</tr>
	<?php
	foreach($tableArray as $key => $value){	
	    $tableName = substr($value,0,strlen($value)-3);  
		echo '<tr><td>'.$key.'</td>';
		echo '<td>'.$tableName.'</td>';
		echo '<td><a href="admin.php?queryTableName='.$tableName.'">数据 | </a>';
		echo '<a href="admin.php?deleteTableName='.$tableName.'">删除</a>';
		echo '</td></tr>';
	}
	?>
</table>

<h3>添加表：</h3>
<form action="admin.php" method="post">
	<input type="text" name="addTableName" />
	<input type="submit" value="添加" />
</form>
<hr />

<h3>数据-表名：<?php echo $queryTableName?></h3>
<table border="1">
	<?php
	echo '<tr>';
	foreach($columArray as $key => $value){
		echo '<td>'.$value.'</td>';
	}
	echo '</tr>';
	
	foreach($dataArray as $key => $value){
		echo '<tr>';
		foreach($value as $valueKey => $valueValue){
			echo '<td>'.$valueValue.'</td>';
		}
	    echo '</tr>';
	}
	?>
</table>

<body>
</html>
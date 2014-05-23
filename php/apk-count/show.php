<?php require 'data.php' ?>
<table border="1">
    <tr><td>apk名称</td><td>安装数</td></tr>
	<?php while($key = key($apk_count)) { ?>
	    <tr><td><?php echo $key; ?></td><td><?php echo $apk_count[$key]; ?></td></tr>
    <?php next($apk_count);} ?>
</table>
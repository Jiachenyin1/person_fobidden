editor:尹佳成
time:2020.12.6
describe:行人安全区域识别

1.requirment
cuda10.1 安装
cudnn7.6.5 安装
python tensorrt>=6.0 安装
ubunutu 18.04 下测试

pytorch>=1.13

opencv>3.0

2.下载person_forbidden文件夹，然后解压

3.运行forbidden.py文件

4.在显示图像中左击区域，确定后右击鼠标，即可划定forbidden区域，当有行人进入禁止区域时，发出forbidden信号（红框产生）

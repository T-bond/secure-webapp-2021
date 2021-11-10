This web application uses an efficient and secure CAFF parser built using C++. For building and more details, please refer to the following document:

https://github.com/aldr4fu/secure-webapp-2021/blob/main/parser/README.md

This page contains some sample outputs produced by the parser. Please find the referenced CAFF files in the `examples` folder.

### 1.caff
command:
```
mkdir build; cd build; cmake ..; make; ./parser ../examples/in/1.caff output.gif
```
output:
```
Parsing CAFF video file...
Author: Test Creator
Date of creation: Thu Jul  2 14:50:00 2020
Resolution: 1000x667
Valid: true
Creating gif segment...
Output written to file 'output.gif'.
```
rendered preview:

![unable to display preview, please check the examples folder](https://github.com/aldr4fu/secure-webapp-2021/blob/main/parser/examples/out/1.gif)
### 2.caff
command:
```
mkdir build; cd build; cmake ..; make; ./parser ../examples/in/2.caff output.gif
```
output:
```
Parsing CAFF video file...
Author: Second_Creator
Date of creation: Thu Jul  2 14:50:00 2020
Resolution: 1000x667
Valid: true
Creating gif segment...
Output written to file 'output.gif'.
```
rendered preview:

![unable to display preview, please check the examples folder](https://github.com/aldr4fu/secure-webapp-2021/blob/main/parser/examples/out/2.gif)
### 3.caff
command:
```
mkdir build; cd build; cmake ..; make; ./parser ../examples/in/3.caff output.gif
```
output:
```
Parsing CAFF video file...
Author: <unknown>
Date of creation: Thu Jul  2 14:50:00 2020
Resolution: 1000x667
Valid: true
Creating gif segment...
Output written to file 'output.gif'.
```
rendered preview:

![unable to display preview, please check the examples folder](https://github.com/aldr4fu/secure-webapp-2021/blob/main/parser/examples/out/3.gif)

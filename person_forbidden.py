# -*- coding: utf-8 -*-
import cv2
import time,os
import numpy as np

tpPointsChoose = []
drawing = False
tempFlag = False
def draw_ROI(event, x, y, flags, param):
    global point1, tpPointsChoose,pts,drawing, tempFlag
    if event == cv2.EVENT_LBUTTONDOWN:
        tempFlag = True
        drawing = False
        point1 = (x, y)
        tpPointsChoose.append((x, y))  # 用于画点
    if event == cv2.EVENT_RBUTTONDOWN:
        tempFlag = True
        drawing = True
        pts = np.array([tpPointsChoose], np.int32)
        # print(pts)
    if event == cv2.EVENT_MBUTTONDOWN:
        tempFlag = False
        drawing = True
        tpPointsChoose = []



def isPoiWithinPoly(poi,poly):
    #输入：点，多边形三维数组
    #poly=[[[x1,y1],[x2,y2],……,[xn,yn],[x1,y1]],[[w1,t1],……[wk,tk]]] 三维数组
    sinsc=0 #交点个数
    for epoly in poly: #循环每条边的曲线->each polygon 是二维数组[[x1,y1],…[xn,yn]]
        for i in range(len(epoly)): #[0,len-1]
            s_poi=epoly[i]
            s_poi_bf = epoly[i-1]
            if i < (len(epoly)-2):  #首先限制下标范围，防止超出
                e_poi = epoly[i + 1]
                e_poi_af = epoly[i + 2]
            elif i == len(epoly)-2: # 若超出循环，则设置为起始值
                e_poi = epoly[-1]
                e_poi_af = epoly[0]
            elif i == len(epoly)-1: # 若超出循环，则设置为起始值
                e_poi = epoly[0]
                e_poi_af = epoly[1]
            if poi[1] == s_poi[1] == e_poi[1]: # 判断平行线段，是否位于区域中间位置，若位于，则应该 +1
                if ((s_poi[1]-s_poi_bf[1])*(e_poi_af[1]-s_poi[1]) > 0):
                    sinsc += 1
                    continue
            elif poi[1] == s_poi[1] != e_poi[1]: # 点
                if ((s_poi_bf[1]-s_poi[1])*(s_poi[1]-e_poi[1])>0):
                    sinsc += 1
                    continue 
            elif s_poi[1] > poi[1] and e_poi[1] > poi[1]:  # 线段在射线上边
                continue
            elif s_poi[1] < poi[1] and e_poi[1] < poi[1]:  # 线段在射线下边
                continue
            elif s_poi[0] < poi[0] and e_poi[1] < poi[1]:  # 线段在射线左边
                continue
            else:
                xseg = e_poi[0] - (e_poi[0] - s_poi[0]) * (e_poi[1] - poi[1]) / (e_poi[1] - s_poi[1])  # 求交
                if xseg < poi[0]:  # 交点在射线起点的左侧
                    continue
                else:
                    sinsc += 1  # 排除上述情况之后
    return True if sinsc%2==1 else  False

if __name__ == "__main__":
    video = 'test.mp4'
    cap = cv2.VideoCapture()  
    cap.open(video)
    im_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    im_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    cv2.namedWindow("result", cv2.WINDOW_NORMAL)
    cv2.setMouseCallback('result',draw_ROI)
    park_ind = 0
    parking_txt = 'parking_plot.txt'
    if os.path.isfile(parking_txt):
        os.remove(parking_txt)
    while cap.grab():
        _, ori_im = cap.retrieve()
        # im = cv2.cvtColor(ori_im, cv2.COLOR_BGR2RGB)
        if (tempFlag == True and drawing == False) :  # 鼠标点击
            cv2.circle(ori_im, point1, 5, (0, 255, 0), 2)  ##绿色
            for i in range(len(tpPointsChoose) - 1):
                cv2.line(ori_im, tpPointsChoose[i], tpPointsChoose[i + 1], (255, 0, 0), 2)  ##蓝色
        if (tempFlag == True and drawing == True):  #鼠标右击
            cv2.polylines(ori_im, pts, True, (0, 0, 255), thickness=2)  ##red
            with open(parking_txt,'a') as f:
                if len(tpPointsChoose)>0:
                    line = str(park_ind) +" " +str(tpPointsChoose[0][0])+" " +str(tpPointsChoose[0][1])+" " +str(tpPointsChoose[1][0])+" " +str(tpPointsChoose[1][1]) +\
                       " " +str(tpPointsChoose[2][0])+" " +str(tpPointsChoose[2][1])+" " +str(tpPointsChoose[3][0])+" " +str(tpPointsChoose[3][1])  +"\n"
                    f.write(line)
                    park_ind += 1
                    tpPointsChoose = []
        cv2.imshow('result', ori_im)
        cv2.waitKey(1000)
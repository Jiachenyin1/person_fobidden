# from hyperlpr_py3 import pipline as pp
import cv2,time
from trt import tensorrt
import numpy as np 
from draw import draw_person
from person_forbidden import isPoiWithinPoly

video = 'forbidden.mp4'
save_path = 'MySaveVideo.avi'
# pp.SimpleRecognizePlate_video(video, save_path)
show_demo = True

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
        print(pts)
    if event == cv2.EVENT_MBUTTONDOWN:
        tempFlag = False
        drawing = True
        tpPointsChoose = []

def run():
    trt_car= tensorrt([608,608])
    cap = cv2.VideoCapture()  
    cap.open(video)
    im_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    im_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    cv2.namedWindow("result", cv2.WINDOW_NORMAL)
    cv2.setMouseCallback('result',draw_ROI)
    fps=0.7 / cap.get(cv2.CAP_PROP_FPS)
    while cap.grab():
        _, ori_im = cap.retrieve()
        bbox_xywh, cls_conf, cls_ids = trt_car.detect(trt_car.context , trt_car.buffers,ori_im,im_width,im_height)
        if len(bbox_xywh)==0:
            continue
        class_det_car = [0]
        save_id = []
        for i, id in enumerate(cls_ids):
            if id not in class_det_car:
                save_id.append(i)
        ##numpy array 进行delete
        bbox_xywh_Person= np.delete(bbox_xywh , [save_id],axis=0)
        cls_conf_Person = np.delete(cls_conf ,[save_id])
        cls_ids = np.delete(cls_ids ,[save_id])
        time.sleep(fps)
        if (tempFlag == True and drawing == False) :  # 鼠标点击
            cv2.circle(ori_im, point1, 5, (0, 255, 0), 2)  ##绿色
            for i in range(len(tpPointsChoose) - 1):
                cv2.line(ori_im, tpPointsChoose[i], tpPointsChoose[i + 1], (255, 0, 0), 2)  ##蓝色
        if (tempFlag == True and drawing == True):  #鼠标右击
            cv2.polylines(ori_im, pts, True, (0, 0, 255), thickness=2)  ##red
            for each_Person in bbox_xywh_Person:
                person = (each_Person[2:4]  + each_Person[0:2] ) / 2
                sign = isPoiWithinPoly(person, pts)
                if (sign== True):
                    cv2.putText(ori_im, 'forbidden', tpPointsChoose[0],cv2.FONT_HERSHEY_SIMPLEX, 2, (255, 0, 0), 2, cv2.LINE_AA)
        if (tempFlag == False and drawing == True):  # 鼠标中键
            for i in range(len(tpPointsChoose) - 1):
                cv2.line(ori_im, tpPointsChoose[i], tpPointsChoose[i + 1], (0, 0, 255), 2)
        draw_person(ori_im, bbox_xywh_Person,cls_conf_Person,cls_ids,show_demo)
        if cv2.waitKey(1)&0xFF == ord('q'): 
            break

if __name__ == "__main__":
    run()
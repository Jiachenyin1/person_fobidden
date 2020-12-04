import numpy as np
import cv2
import numpy as np
import hyperlpr 
import os
from PIL import Image
from PIL import ImageDraw
import json


PATH=os.path.dirname(hyperlpr.__file__)#CURPATH,'lib/hyperlpr/models')
PATH=os.path.join(PATH,'models')
PR = hyperlpr.LPR(PATH)

palette = (2 ** 11 - 1, 2 ** 15 - 1, 2 ** 20 - 1)

from PIL import ImageFont
fontC = ImageFont.truetype("./Font/platech.ttf", 14, 0);

def compute_color_for_labels(label):
    """
    Simple function that adds fixed color depending on the class
    """
    color = [int((p * (label ** 2 - label + 1)) % 255) for p in palette]
    return tuple(color)


def drawRectBox(image,rect,addText):
    # cv2.rectangle(image, (int(rect[0]), int(rect[1])), (int( rect[2]), int(rect[3])), (0,0, 255), 2, cv2.LINE_AA)
    cv2.rectangle(image, (int(rect[0]-1), int(rect[1])-16), (int(rect[0] + 115), int(rect[1])), (0, 0, 255), -1, cv2.LINE_AA)

    img = Image.fromarray(image)
    draw = ImageDraw.Draw(img)
    #draw.text((int(rect[0]+1), int(rect[1]-16)), addText.decode("utf-8"), (255, 255, 255), font=fontC)
    draw.text((int(rect[0]+1), int(rect[1]-16)), addText, (255, 255, 255),font = fontC)
    imagex = np.array(img)

    return imagex


def draw_boxes(img, bbox_xywh_Car=[],cls_conf_Car=[],cls_ids=[],show_demo=False,detected_path=str,offset=(0,0) ):
    car_name = {2:"car" , 5:'bus' , 7:'truck'}
    jsons = []
    for i,box in enumerate(bbox_xywh_Car):
        res_json = {}
        x1,y1,x2,y2 = [int(i) if i>=0 else 0 for i in box ]        
        # box text and bar
        id = cls_conf_Car[i] if cls_conf_Car is not None else 0    
        color = compute_color_for_labels(id)
        name = car_name[cls_ids[i]]
        label = '{}{}:{:0.2f}'.format("",name, id)
        t_size = cv2.getTextSize(label, cv2.FONT_HERSHEY_PLAIN, 1 , 1)[0]
        cv2.rectangle(img,(x1, y1),(x2,y2),color,1)
        cv2.rectangle(img,(x1, y1),(x1+t_size[0]+3,y1+t_size[1]+4), color,-1)  ##填充
        cv2.putText(img,label,(x1,y1+t_size[1]+4), cv2.FONT_HERSHEY_PLAIN, 1, [255,255,255], 1)
        img_crop=img[y1:y2,x1:x2]
        # image.append()
        images = PR.plate_recognition(img_crop)
        for j,plate in enumerate(images):
                plate, confidence, rect = plate
                rect[0] ,rect[2] =  rect[0]+x1 ,rect[2]+x1
                rect[1] ,rect[3] =  rect[1]+y1 ,rect[3]+y1
                if confidence>0.8:
                    # 绘制车牌识别框
                    img = drawRectBox(img, rect, plate) 
                    res_json["Name"] = plate
                    res_json["Confidence"] = confidence
                    res_json["x"] = int(rect[0])
                    res_json["y"] = int(rect[1])
                    res_json["w"] = int(rect[2])
                    res_json["h"] = int(rect[3])
                    jsons.append(res_json)
    print(json.dumps(jsons,ensure_ascii=False))
    print("depath-----------",detected_path)
    if show_demo:
        cv2.imshow('result', img)
        cv2.waitKey(1)
    else:
        cv2.imwrite(detected_path, img)
    return json.dumps(jsons,ensure_ascii=False)

def draw_person(img, bbox_xywh_Person=[],cls_conf_Person=[],cls_ids=[],offset=(0,0) ):
    car_name = {0:"person"}
    for i,box in enumerate(bbox_xywh_Person):
        x1,y1,x2,y2 = [int(i) if i>=0 else 0 for i in box ]        
        # box text and bar
        id = cls_conf_Person[i] if cls_conf_Person is not None else 0    
        color = compute_color_for_labels(id)
        name = car_name[cls_ids[i]]
        label = '{}{}:{:0.2f}'.format("",name, id)
        t_size = cv2.getTextSize(label, cv2.FONT_HERSHEY_PLAIN, 1 , 1)[0]
        cv2.rectangle(img,(x1, y1),(x2,y2),color,1)
        cv2.rectangle(img,(x1, y1),(x1+t_size[0]+3,y1+t_size[1]+4), color,-1)  ##填充
        cv2.putText(img,label,(x1,y1+t_size[1]+4), cv2.FONT_HERSHEY_PLAIN, 1, [255,255,255], 1)
        cv2.imshow('result', img)
    return 




if __name__ == '__main__':
    for i in range(82):
        print(compute_color_for_labels(i))

from pygame import *
from glob import *

pics = glob("*.gif")
imgs = [image.load(p) for p in pics]
c=0
for i in imgs:
    image.save(i, pics[c].replace(".gif",".png"))
    print(pics[c])
    c+=1

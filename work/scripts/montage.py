import sys
from PIL import Image
out=sys.argv[1]; ims=[Image.open(f) for f in sys.argv[2:]]
w,h=ims[0].size;o=Image.new('RGB',(w*len(ims)+4*len(ims),h))
for i,m in enumerate(ims):o.paste(m,(i*(w+4),0))
o.save(out)

import sys,glob,collections
c=collections.Counter()
for f in glob.glob(sys.argv[1]+'/t/*.tdf'):
    b=open(f,'rb').read(); n=b[0]; p=1
    for i in range(n):
        L=b[p]<<8|b[p+1]; p+=2; s=b[p:p+L]; p+=L
        for j,x in enumerate(s):
            if x>=0x80: c[(f.split('/')[-1],hex(x),s[j+1] if j+1<len(s) else None)]+=1
    if p!=len(b): print('trail',f,len(b)-p)
print(c.most_common(30))

"""Parser file map /m/m*/*.m cua Heroes Lore Zero (theo g.a(byte[]) + db.int_a + cb.a).
parse(b) -> dict(names=[bytes|None]*2, name_pos, table_pos, strings=[bytes], seps=[int])"""
def s8(x): return x-256 if x>127 else x
def parse(b):
    p=3; names=[]; name_start=p
    for i in range(2):
        L=s8(b[p]); p+=1
        if L>0: names.append(b[p:p+L]); p+=L
        else: names.append(None)
    name_end=p
    n=s8(b[p]); p+=1; p+=max(n,0)          # tile objects
    w=s8(b[p]); h=s8(b[p+1]); p+=2; p+=2*w*h
    for k in range(3):
        n=b[p]; p+=1; p+=5*n
    n=s8(b[p]); p+=1; p+=max(n,0)           # enemies
    n=s8(b[p]); p+=1
    for i in range(max(n,0)):
        f=b[p+1]; p+=3+(1 if f&0x80 else 0)
    n3=s8(b[p]); p+=1; p+=max(n3,0)         # npc
    n=s8(b[p]); p+=1
    for i in range(max(n,0)):
        f=b[p+1]; p+=3+(1 if f&0x80 else 0)
    for mode in (1,2):                       # cb x2
        c=b[p]; p+=1; tot=0
        for i in range(c):
            s=b[p]
            if mode>1: p+=1; s+=b[p]<<8
            tot+=s; p+=1
        p+=tot
    tp=p; n=b[p]; p+=1
    lens=list(b[p:p+n]); p+=n
    strs=[];seps=[]
    if n and b[-1]==0:
        # chuoi cach nhau bang 0 -> tach theo 0 (sua m/m/22.m: chuoi > 255 byte lam lech do dai u1)
        strs=b[p:-1].split(b'\0'); assert len(strs)==n,(len(strs),n); seps=[0]*n; p=len(b)
        for L,x in zip(lens,strs): assert L==len(x)&0xFF
    else:
        for L in lens:
            strs.append(b[p:p+L]); p+=L; seps.append(b[p] if p<len(b) else None); p+=1
    return dict(names=names,name_start=name_start,name_end=name_end,table_pos=tp,strings=strs,seps=seps,end=p,size=len(b))
def build_new(b,info,names,strs):
    '''Dinh dang moi (ScriptLoader): so chuoi u2 + do dai u2, moi chuoi + byte 0.'''
    out=bytearray(b[:info['name_start']])
    for s in names:
        if s is None: out.append(0)
        else:
            assert 0<len(s)<128,len(s); out.append(len(s)); out+=s
    out+=b[info['name_end']:info['table_pos']]
    out+=len(strs).to_bytes(2,'big')
    for s in strs: out+=len(s).to_bytes(2,'big')
    for s in strs: out+=s+b'\0'
    return bytes(out)
def build(b,info,names,strs):
    out=bytearray(b[:info['name_start']])
    for s in names:
        if s is None: out.append(0)
        else:
            assert 0<len(s)<128,len(s); out.append(len(s)); out+=s
    out+=b[info['name_end']:info['table_pos']]
    out.append(len(strs))
    for s in strs: assert len(s)<256,(len(s),s); out.append(len(s))
    for s,sep in zip(strs,info['seps']):
        out+=s
        if sep is not None: out.append(sep)
    return bytes(out)
if __name__=='__main__':
    import sys,glob
    for f in sorted(glob.glob(sys.argv[1]+'/m/m*/*.m')):
        b=open(f,'rb').read(); i=parse(b)
        ok=build(b,i,i['names'],i['strings'])==b
        print(f.split('/m/')[-1],len(i['strings']),i['end']-i['size'],set(i['seps']),ok,i['names'][0])

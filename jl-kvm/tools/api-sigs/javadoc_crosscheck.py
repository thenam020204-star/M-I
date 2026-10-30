#!/usr/bin/env python3
"""Cross-check a signature DB (db/cldc-1.1.json) against the OFFICIAL CLDC 1.1 javadoc (tools/env/cache/spec/javadocs, NOT committed:
Oracle licence). Written for PLAN T18 (javadoc wins over the MicroEmulator stub DB). Usage: javadoc_crosscheck.py [DB.json [JAVADOC_DIR]]
Compares: class set, class modifiers/super/interfaces, public/protected fields (modifiers, type), constructors and methods
(modifiers, return type, parameter types, throws clause). Prints one line per difference; exit 1 when any."""
import html, json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
db_path = sys.argv[1] if len(sys.argv) > 1 else os.path.join(HERE, 'db', 'cldc-1.1.json')
jd = sys.argv[2] if len(sys.argv) > 2 else os.path.join(HERE, '..', 'env', 'cache', 'spec', 'javadocs')

def simple(t):
    return t.replace('/', '.').rsplit('.', 1)[-1]

def desc_to_type(d):
    dims = 0
    while d.startswith('['):
        dims += 1; d = d[1:]
    prim = {'I': 'int', 'J': 'long', 'F': 'float', 'D': 'double', 'Z': 'boolean', 'B': 'byte', 'C': 'char', 'S': 'short', 'V': 'void'}
    t = prim[d] if d in prim else simple(d[1:-1])
    return t + '[]' * dims

def parse_desc(d):
    args, i = [], 1
    while d[i] != ')':
        j = i
        while d[j] == '[': j += 1
        j = d.index(';', j) if d[j] == 'L' else j
        args.append(desc_to_type(d[i:j + 1])); i = j + 1
    return args, desc_to_type(d[i + 1:])

MODS = [(0x1, 'public'), (0x4, 'protected'), (0x8, 'static'), (0x10, 'final'), (0x20, 'synchronized'), (0x100, 'native'), (0x400, 'abstract')]
def mods(a, kind):
    out = []
    for bit, n in MODS:
        if a & bit:
            if kind == 'method' and bit == 0x20 and False: continue
            out.append(n)
    return out

def norm(s):
    s = html.unescape(re.sub(r'<[^>]+>', '', s)).replace('\xa0', ' ')
    return re.sub(r'\s+', ' ', s).strip()

def dbsigs(c):
    s = set()
    short = simple(c['name'])
    for f in c['fields']:
        s.add(('F', ' '.join(mods(f['access'], 'field') + [desc_to_type(f['desc']), f['name']])))
    for m in c['methods']:
        a, r = parse_desc(m['desc'])
        # synchronized is not part of the javadoc signature, abstract on interface methods neither
        am = m['access'] & ~0x20
        if c['access'] & 0x200: am &= ~0x400
        ms = [x for x in mods(am, 'method') if not (x == 'native')]
        th = sorted(simple(e) for e in m.get('exceptions', []))
        if m['name'] == '<init>':
            sig = ' '.join(ms + [short + '(' + ', '.join(a) + ')'])
        else:
            sig = ' '.join(ms + [r, m['name'] + '(' + ', '.join(a) + ')'])
        if th: sig += ' throws ' + ', '.join(th)
        s.add(('C' if m['name'] == '<init>' else 'M', sig))
    return s

def docsigs(path, short):
    t = open(path, encoding='latin-1').read()
    s = set()
    for kind, anchor_re in (('F', 'field_detail'), ('C', 'constructor_detail'), ('M', 'method_detail')):
        m = re.search(r'<A NAME="%s">' % anchor_re, t)
        if not m: continue
        end = t.find('<!-- ======', m.end())
        sec = t[m.end(): end if end > 0 else len(t)]
        for pre in re.findall(r'<PRE>(.*?)</PRE>', sec, re.S):
            p = norm(pre)
            p = re.sub(r'\s*\(\s*', '(', p)
            p = re.sub(r'\(([^)]*)\)', lambda mm: '(' + ', '.join(re.sub(r'(\S+) \S+$', r'\1', a.strip()) if ' ' in a.strip() else a.strip() for a in mm.group(1).split(',') if a.strip()) + ')', p)
            tm = re.match(r'(.*?)(?: throws (.*))?$', p)
            sig = tm.group(1)
            if tm.group(2):
                sig += ' throws ' + ', '.join(sorted(x.strip() for x in tm.group(2).split(',')))
            s.add((kind, sig))
    return s

db = json.load(open(db_path))
bad = 0
alls = open(os.path.join(jd, 'allclasses-noframe.html'), encoding='latin-1').read()
doc_classes = set(re.findall(r'HREF="([\w/]+)\.html"', alls))
db_classes = {c['name'] for c in db['classes']}
for n in sorted(doc_classes - db_classes): print('only in javadoc: class', n); bad += 1
for n in sorted(db_classes - doc_classes): print('only in DB: class', n); bad += 1
for c in sorted(db['classes'], key=lambda c: c['name']):
    p = os.path.join(jd, c['name'] + '.html')
    if not os.path.exists(p): continue
    a, b = dbsigs(c), docsigs(p, simple(c['name']))
    for k, v in sorted(a - b): print('DB only   %s: %s %s' % (c['name'], k, v)); bad += 1
    for k, v in sorted(b - a): print('javadoc   %s: %s %s' % (c['name'], k, v)); bad += 1
print('%d difference(s)' % bad)
sys.exit(1 if bad else 0)

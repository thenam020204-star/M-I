import sys, zipfile, os, struct
src = sys.argv[1]
tmp = src + ".tmp"
with zipfile.ZipFile(src) as zi, zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as zo:
    for it in zi.infolist():
        zo.writestr(it, zi.read(it.filename), compress_type=zipfile.ZIP_DEFLATED)
    zo.writestr("data.txt", "good data, good data, good data, good data\n")
    zo.writestr("other.txt", "second file\n")
os.replace(tmp, src)
# damage the deflated payload of data.txt; the CRC stored in the headers no longer matches the inflated bytes
with zipfile.ZipFile(src) as z:
    info = z.getinfo("data.txt")
b = bytearray(open(src, "rb").read())
off = info.header_offset
nlen, elen = struct.unpack("<HH", b[off + 26:off + 30])
payload = off + 30 + nlen + elen
b[payload + 4] ^= 0x01
open(src, "wb").write(b)

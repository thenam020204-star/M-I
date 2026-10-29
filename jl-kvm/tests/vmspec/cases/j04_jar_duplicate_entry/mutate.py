import sys, zipfile, warnings
warnings.simplefilter("ignore")
with zipfile.ZipFile(sys.argv[1], "a") as z:
    z.writestr("data.txt", "FIRST\n")
    z.writestr("data.txt", "SECOND\n")      # two central-directory entries with the same name

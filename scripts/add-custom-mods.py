"""Adds the custom mods built in this repo to the exported .mrpack as overrides,
so one file contains the whole pack (Modrinth mods by download + our own jars)."""
import glob
import os
import sys
import zipfile

packs = glob.glob("pack/*.mrpack")
if not packs:
    sys.exit("no .mrpack found in pack/")
mrpack = packs[0]
tmp = mrpack + ".tmp"

added = []
with zipfile.ZipFile(mrpack) as zin, zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as zout:
    for item in zin.infolist():
        zout.writestr(item, zin.read(item.filename))
    for jar in sorted(glob.glob("mods/*/build/libs/*.jar")):
        if jar.endswith(("-sources.jar", "-dev.jar")):
            continue
        zout.write(jar, "overrides/mods/" + os.path.basename(jar))
        added.append(os.path.basename(jar))
os.replace(tmp, mrpack)
print("added custom mods:", added)
if not added:
    sys.exit("no custom mod jars found, did the build run?")

# -*- coding: utf-8 -*-
"""Glyph specification for font_10px_v2 (Vietnamese diacritics redrawn).

Cell = 10 rows.  Row map (unchanged from the old 10px cut):
    0,1   diacritic zone      3-7  x-height        8,9  descender
    2     mandatory gap row   1-7  plain capitals
Accented capitals are shortened to 6 rows (2-7) to free the mark zone.
"""

# ---------------------------------------------------------------- mark shapes
# each mark = (list of (row, col) inside its own box, box width)
ACUTE  = ([(0, 1), (1, 0)], 2)                    # .#  /  #.
GRAVE  = ([(0, 0), (1, 1)], 2)                    # #.  /  .#
HOOK   = ([(0, 0), (0, 1), (1, 1)], 2)            # ##  /  .#
TILDE  = ([(0, 1), (0, 2), (1, 0), (1, 1)], 3)    # .## /  ##.   (stacked, narrow)
TILDE_W = ([(0, 0), (0, 1), (0, 3), (1, 0), (1, 2), (1, 3)], 4)  # ##.# / #.##
CIRC   = ([(0, 1), (0, 2), (1, 0), (1, 3)], 4)    # .##. /  #..#   wide chevron
BREVE  = ([(0, 0), (0, 3), (1, 1), (1, 2)], 4)    # #..# /  .##.   wide cup

TONES  = {'acute': ACUTE, 'grave': GRAVE, 'hook': HOOK, 'tilde': TILDE_W}   # solo
TONES_S = {'acute': ACUTE, 'grave': GRAVE, 'hook': HOOK, 'tilde': TILDE}    # stacked
CIRC_N = ([(0, 1), (1, 0), (1, 2)], 3)            # .#. /  #.#   compact
BREVE_N = ([(0, 0), (0, 2), (1, 1)], 3)           # #.# /  .#.

HATS  = {'circ': CIRC, 'breve': BREVE}
HATS_N = {'circ': CIRC_N, 'breve': BREVE_N}

# stacked tone: 3-wide box placed immediately right of the hat, so hat+tone
# read as ONE mark cluster over the letter.  Cell (1,0) is never used -> it can
# never fuse with the hat's right leg.
STACK_TONES = {
    'acute': [(0, 1), (1, 0)],
    'grave': [(0, 0), (1, 1)],
    'hook':  [(0, 0), (0, 1), (1, 1)],
    'tilde': [(0, 1), (0, 2), (1, 0), (1, 1)],
}

# ------------------------------------------------------------- 6-row capitals
# rows 2..7, top to bottom
SHORT_CAPS = {
 'A': ("..##...", ".#..#..", ".#..#..", "######.", "#....#.", "#....#."),
 'E': ("#####.", "#.....", "####..", "#.....", "#.....", "#####."),
 'I': ("###.", ".#..", ".#..", ".#..", ".#..", "###."),
 'O': ("..###...", ".#...#..", "#.....#.", "#.....#.", ".#...#..", "..###..."),
 'U': ("#....#.", "#....#.", "#....#.", "#....#.", "#....#.", ".####.."),
 'Y': ("#...#.", ".#.#..", ".#.#..", "..#...", "..#...", "..#..."),
 'O_horn': ("..###..#", ".#...##.", "#.....#.", "#.....#.", ".#...#..", "..###..."),
 'U_horn': ("#....##", "#....#.", "#....#.", "#....#.", "#....#.", ".####.."),
}

# ------------------------------------------------------------- 5-row capitals
# rows 3..7 -- leaves row 2 empty so the mark never touches the letter
SHORT_CAPS5 = {
 'A': ("..##...", ".#..#..", "######.", "#....#.", "#....#."),
 'E': ("#####.", "#.....", "####..", "#.....", "#####."),
 'I': ("###.", ".#..", ".#..", ".#..", "###."),
 'O': ("..###...", ".#...#..", "#.....#.", ".#...#..", "..###..."),
 'U': ("#....#.", "#....#.", "#....#.", "#....#.", ".####.."),
 'Y': ("#...#.", ".#.#..", "..#...", "..#...", "..#..."),
 'O_horn': ("..###..#", ".#...##.", "#.....#.", ".#...#..", "..###..."),
 'U_horn': ("#....##", "#....#.", "#....#.", "#....#.", ".####.."),
}

# --------------------------------------------------------------- letter table
# (char, base, hat, tone, dot_below)
VOWELS = [
    ('a', 'a', None, None, 0), ('á', 'a', None, 'acute', 0),
    ('à', 'a', None, 'grave', 0), ('ả', 'a', None, 'hook', 0),
    ('ã', 'a', None, 'tilde', 0), ('ạ', 'a', None, None, 1),
    ('ă', 'a', 'breve', None, 0), ('ắ', 'a', 'breve', 'acute', 0),
    ('ằ', 'a', 'breve', 'grave', 0), ('ẳ', 'a', 'breve', 'hook', 0),
    ('ẵ', 'a', 'breve', 'tilde', 0), ('ặ', 'a', 'breve', None, 1),
    ('â', 'a', 'circ', None, 0), ('ấ', 'a', 'circ', 'acute', 0),
    ('ầ', 'a', 'circ', 'grave', 0), ('ẩ', 'a', 'circ', 'hook', 0),
    ('ẫ', 'a', 'circ', 'tilde', 0), ('ậ', 'a', 'circ', None, 1),

    ('é', 'e', None, 'acute', 0), ('è', 'e', None, 'grave', 0),
    ('ẻ', 'e', None, 'hook', 0), ('ẽ', 'e', None, 'tilde', 0),
    ('ẹ', 'e', None, None, 1),
    ('ê', 'e', 'circ', None, 0), ('ế', 'e', 'circ', 'acute', 0),
    ('ề', 'e', 'circ', 'grave', 0), ('ể', 'e', 'circ', 'hook', 0),
    ('ễ', 'e', 'circ', 'tilde', 0), ('ệ', 'e', 'circ', None, 1),

    ('í', 'i', None, 'acute', 0), ('ì', 'i', None, 'grave', 0),
    ('ỉ', 'i', None, 'hook', 0), ('ĩ', 'i', None, 'tilde', 0),
    ('ị', 'i', None, None, 1),

    ('ó', 'o', None, 'acute', 0), ('ò', 'o', None, 'grave', 0),
    ('ỏ', 'o', None, 'hook', 0), ('õ', 'o', None, 'tilde', 0),
    ('ọ', 'o', None, None, 1),
    ('ô', 'o', 'circ', None, 0), ('ố', 'o', 'circ', 'acute', 0),
    ('ồ', 'o', 'circ', 'grave', 0), ('ổ', 'o', 'circ', 'hook', 0),
    ('ỗ', 'o', 'circ', 'tilde', 0), ('ộ', 'o', 'circ', None, 1),
    ('ơ', 'o_horn', None, None, 0), ('ớ', 'o_horn', None, 'acute', 0),
    ('ờ', 'o_horn', None, 'grave', 0), ('ở', 'o_horn', None, 'hook', 0),
    ('ỡ', 'o_horn', None, 'tilde', 0), ('ợ', 'o_horn', None, None, 1),

    ('ú', 'u', None, 'acute', 0), ('ù', 'u', None, 'grave', 0),
    ('ủ', 'u', None, 'hook', 0), ('ũ', 'u', None, 'tilde', 0),
    ('ụ', 'u', None, None, 1),
    ('ư', 'u_horn', None, None, 0), ('ứ', 'u_horn', None, 'acute', 0),
    ('ừ', 'u_horn', None, 'grave', 0), ('ử', 'u_horn', None, 'hook', 0),
    ('ữ', 'u_horn', None, 'tilde', 0), ('ự', 'u_horn', None, None, 1),

    ('ý', 'y', None, 'acute', 0), ('ỳ', 'y', None, 'grave', 0),
    ('ỷ', 'y', None, 'hook', 0), ('ỹ', 'y', None, 'tilde', 0),
    ('ỵ', 'y', None, None, 1),

    ('Á', 'A', None, 'acute', 0), ('À', 'A', None, 'grave', 0),
    ('Ả', 'A', None, 'hook', 0), ('Ã', 'A', None, 'tilde', 0),
    ('Ạ', 'A', None, None, 1),
    ('Ă', 'A', 'breve', None, 0), ('Ắ', 'A', 'breve', 'acute', 0),
    ('Ằ', 'A', 'breve', 'grave', 0), ('Ẳ', 'A', 'breve', 'hook', 0),
    ('Ẵ', 'A', 'breve', 'tilde', 0), ('Ặ', 'A', 'breve', None, 1),
    ('Â', 'A', 'circ', None, 0), ('Ấ', 'A', 'circ', 'acute', 0),
    ('Ầ', 'A', 'circ', 'grave', 0), ('Ẩ', 'A', 'circ', 'hook', 0),
    ('Ẫ', 'A', 'circ', 'tilde', 0), ('Ậ', 'A', 'circ', None, 1),

    ('É', 'E', None, 'acute', 0), ('È', 'E', None, 'grave', 0),
    ('Ẻ', 'E', None, 'hook', 0), ('Ẽ', 'E', None, 'tilde', 0),
    ('Ẹ', 'E', None, None, 1),
    ('Ê', 'E', 'circ', None, 0), ('Ế', 'E', 'circ', 'acute', 0),
    ('Ề', 'E', 'circ', 'grave', 0), ('Ể', 'E', 'circ', 'hook', 0),
    ('Ễ', 'E', 'circ', 'tilde', 0), ('Ệ', 'E', 'circ', None, 1),

    ('Í', 'I', None, 'acute', 0), ('Ì', 'I', None, 'grave', 0),
    ('Ỉ', 'I', None, 'hook', 0), ('Ĩ', 'I', None, 'tilde', 0),
    ('Ị', 'I', None, None, 1),

    ('Ó', 'O', None, 'acute', 0), ('Ò', 'O', None, 'grave', 0),
    ('Ỏ', 'O', None, 'hook', 0), ('Õ', 'O', None, 'tilde', 0),
    ('Ọ', 'O', None, None, 1),
    ('Ô', 'O', 'circ', None, 0), ('Ố', 'O', 'circ', 'acute', 0),
    ('Ồ', 'O', 'circ', 'grave', 0), ('Ổ', 'O', 'circ', 'hook', 0),
    ('Ỗ', 'O', 'circ', 'tilde', 0), ('Ộ', 'O', 'circ', None, 1),
    ('Ơ', 'O_horn', None, None, 0), ('Ớ', 'O_horn', None, 'acute', 0),
    ('Ờ', 'O_horn', None, 'grave', 0), ('Ở', 'O_horn', None, 'hook', 0),
    ('Ỡ', 'O_horn', None, 'tilde', 0), ('Ợ', 'O_horn', None, None, 1),

    ('Ú', 'U', None, 'acute', 0), ('Ù', 'U', None, 'grave', 0),
    ('Ủ', 'U', None, 'hook', 0), ('Ũ', 'U', None, 'tilde', 0),
    ('Ụ', 'U', None, None, 1),
    ('Ư', 'U_horn', None, None, 0), ('Ứ', 'U_horn', None, 'acute', 0),
    ('Ừ', 'U_horn', None, 'grave', 0), ('Ử', 'U_horn', None, 'hook', 0),
    ('Ữ', 'U_horn', None, 'tilde', 0), ('Ự', 'U_horn', None, None, 1),

    ('Ý', 'Y', None, 'acute', 0), ('Ỳ', 'Y', None, 'grave', 0),
    ('Ỷ', 'Y', None, 'hook', 0), ('Ỹ', 'Y', None, 'tilde', 0),
    ('Ỵ', 'Y', None, None, 1),
]

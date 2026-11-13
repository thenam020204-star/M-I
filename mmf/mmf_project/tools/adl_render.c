/* Render a MIDI file to raw stereo s16le with libADLMIDI (default embedded bank 0 = DMX/GM bank, OPL3 4-op ON).
   build: gcc -O2 adl_render.c -I<libADLMIDI>/include <libADLMIDI>/build/libADLMIDI.a -lstdc++ -lm -o adl_render
   usage: adl_render in.mid out.raw [rate] [bank] */
#include <stdio.h>
#include <stdlib.h>
#include <adlmidi.h>
int main(int argc, char **argv) {
    int rate = argc > 3 ? atoi(argv[3]) : 44100, bank = argc > 4 ? atoi(argv[4]) : 0;
    struct ADL_MIDIPlayer *p = adl_init(rate);
    if (!p) return 1;
    adl_setBank(p, bank);
    adl_setNumChips(p, 2);
    adl_setSoftPanEnabled(p, 1);
    adl_setLoopEnabled(p, 0);
    if (adl_openFile(p, argv[1]) < 0) { fprintf(stderr, "open: %s\n", adl_errorInfo(p)); return 2; }
    FILE *f = fopen(argv[2], "wb");
    short buf[4096 * 2]; struct ADLMIDI_AudioFormat fmt = {ADLMIDI_SampleType_S16, sizeof(short), 2 * sizeof(short)};
    int n;
    while ((n = adl_playFormat(p, 4096 * 2, (ADL_UInt8 *)buf, (ADL_UInt8 *)(buf + 1), &fmt)) > 0) fwrite(buf, 2, n, f);
    fclose(f); adl_close(p); return 0;
}

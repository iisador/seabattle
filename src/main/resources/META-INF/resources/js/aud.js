class Sounds {
    #bulks = [];
    #bangs = [];
    #kills = [];
    #finishes = [];
    #fails = [];
    #starts = [];

    #nowPlaying;

    constructor() {
        this.#bulks.push(new Audio('/audio/bulk1.ogg'));
        this.#bulks.push(new Audio('/audio/bulk2.ogg'));
        this.#bulks.push(new Audio('/audio/bulk3.ogg'));
        this.#bulks.push(new Audio('/audio/bulk4.ogg'));

        this.#bangs.push(new Audio('/audio/bang1.ogg'));
        this.#bangs.push(new Audio('/audio/bang2.ogg'));
        this.#bangs.push(new Audio('/audio/bang3.ogg'));

        this.#kills.push(new Audio('/audio/kill1.ogg'));

        this.#finishes.push(new Audio('/audio/finish.ogg'));

        this.#fails.push(new Audio('/audio/fail1.ogg'));
        this.#fails.push(new Audio('/audio/fail2.ogg'));

        this.#starts.push(new Audio('/audio/start.ogg'));
    }

    playRandom(arr) {
        const i = random(arr.length);
        if (this.#nowPlaying) {
            this.#nowPlaying.pause();
            this.#nowPlaying.currentTime = 0;
        }
        this.#nowPlaying = arr[i];
        this.#nowPlaying.play();
    }

    playRandomBang() {
        this.playRandom(this.#bangs);
    }

    playRandomBulk() {
        this.playRandom(this.#bulks);
    }

    playRandomKill() {
        this.playRandom(this.#kills);
    }

    playRandomFinish() {
        this.playRandom(this.#finishes);
    }

    playRandomFail() {
        this.playRandom(this.#fails);
    }

    playRandomStart() {
        this.playRandom(this.#starts);
    }
}

class Sounds {
    #bulks = [];
    #bangs = [];
    #kills = [];
    #finishes = [];
    #fails = [];
    #starts = [];
    #custom = [];
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

        this.#custom.push(new Audio("/audio/it's_getting_boring.mp3"));
        this.#custom.push(new Audio("/audio/where's_the_map_Billy.mp3"));
        this.#custom.push(new Audio("/audio/you're_firmly_aground.mp3"));
        this.#custom.push(new Audio("/audio/this_is_our_last_chance.mp3"));
        this.#custom.push(new Audio("/audio/i_vote_to_kill.mp3"));
        this.#custom.push(new Audio("/audio/i_don't_understand_sir.mp3"));
        this.#custom.push(new Audio("/audio/i_don't_like_anything_at_all.mp3"));
    }

    playRandom(arr) {
        const i = random(arr.length);
        if (this.#nowPlaying) {
            this.#nowPlaying.pause();
            this.#nowPlaying.currentTime = 0;
        }
        this.#nowPlaying = arr[i];
        this.#nowPlaying.volume = 0.5;
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

    playItsGettingBoring() {
        this.playRandom([this.#custom[0]]);
    }

    playWhereIsTheMap() {
        this.playRandom([this.#custom[1]]);
    }

    playYouReFirmlyAground() {
        this.playRandom([this.#custom[2]]);
    }

    playThisIsOurLastChance() {
        this.playRandom([this.#custom[3]]);
    }

    playIVoteToKill() {
        this.playRandom([this.#custom[4]]);
    }

    playIDontUnderstandSir() {
        this.playRandom([this.#custom[5]]);
    }

    playIDontLikeAnythingAtAll() {
        this.playRandom([this.#custom[6]]);
    }

    playRandomStart() {
        this.playRandom(this.#starts);
    }
}

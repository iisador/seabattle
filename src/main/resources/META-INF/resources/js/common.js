function isRcrPlayer(winner) {
    return winner && winner.toLowerCase().startsWith('рцр ');
}

function random(max) {
    return Math.floor(Math.random() * max);
}

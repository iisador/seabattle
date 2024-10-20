class Block {

    #blockDiv;
    #x;
    #y;
    #blockBasicClass;

    constructor(blockDiv, letters, blockBasicClass) {
        this.#blockDiv = blockDiv;
        this.#blockDiv.ondragstart = function () {
            return false;
        };
        const slices = blockDiv.id.split('_');
        this.#x = parseInt(slices[1]);
        this.#y = parseInt(slices[2]);
        this.#blockBasicClass = blockBasicClass;
    }

    getX() {
        return this.#x;
    }

    getY() {
        return this.#y;
    }

    mark() {
        this.#blockDiv.className = 'blockMarked';
    }

    restore() {
        if (this.#blockDiv) {
            this.#blockDiv.className = this.#blockBasicClass;
        }
    }

    isOneRow(block) {
        return block.getX() === this.#x;
    }

    isOneColumn(block) {
        return block.getY() === this.#y;
    }

    getLength(block) {
        if (this.isOneRow(block)) {
            return Math.abs(this.#y - block.getY()) + 1;
        }

        if (this.isOneColumn(block)) {
            return Math.abs(this.#x - block.getX()) + 1;
        }
    }

    getStartBlock(block) {
        if (this.isOneRow(block) && (this.#y < block.getY())) {
            return this;
        }

        if (this.isOneColumn(block) && (this.#x < block.getX())) {
            return this;
        }

        return block;
    }

    wasFired() {
        return this.#blockDiv.children.length > 0
            || this.#blockDiv.childNodes.length > 0
            || this.#blockDiv.className === 'enemyShip';
    }
}

class Field {

    #prefix;
    #letters;
    #fieldSize;
    #cellSize;

    #battlefieldDiv;
    #blockBasicClass;

    constructor(fieldSize, cellSize, prefix, active = true) {
        this.#fieldSize = fieldSize;
        this.#cellSize = cellSize;
        if (!prefix) {
            this.#prefix = 'bla|';
        } else {
            this.#prefix = prefix + '|';
        }
        this.#blockBasicClass = active ? 'block' : 'simpleBlock';

        this.#letters = new Map();
        this.#letters.set(1, 'а');
        this.#letters.set(2, 'б');
        this.#letters.set(3, 'в');
        this.#letters.set(4, 'г');
        this.#letters.set(5, 'д');
        this.#letters.set(6, 'е');
        this.#letters.set(7, 'ж');
        this.#letters.set(8, 'з');
        this.#letters.set(9, 'и');
        this.#letters.set(10, 'к');
        this.#letters.set(11, 'л');
        this.#letters.set(12, 'м');
        this.#letters.set(13, 'н');
        this.#letters.set(14, 'о');
        this.#letters.set(15, 'п');
        this.#letters.set(16, 'р');
        this.#letters.set(17, 'с');
        this.#letters.set(18, 'т');
        this.#letters.set(19, 'у');
        this.#letters.set(20, 'ф');
        this.#letters.set(21, 'х');
        this.#letters.set(22, 'ц');
        this.#letters.set(23, 'ч');
        this.#letters.set(24, 'ш');
        this.#letters.set(25, 'щ');
        this.#letters.set(26, 'ъ');
        this.#letters.set(27, 'ы');
        this.#letters.set(28, 'ь');
        this.#letters.set(29, 'э');
        this.#letters.set(30, 'ю');
        this.#letters.set(31, 'я');
    }

    getFieldSize() {
        return this.#fieldSize;
    }

    drawField(holder) {
        this.#battlefieldDiv = document.createElement('div');
        this.#battlefieldDiv.id = this.#prefix + 'battlefield';
        this.#battlefieldDiv.className = this.#prefix + 'battlefield';
        this.#battlefieldDiv.style.display = 'grid';
        this.#battlefieldDiv.style.gridTemplateColumns = 'repeat(' + (this.#fieldSize + 1) + ', ' + this.#cellSize + ')';
        this.#battlefieldDiv.style.gridTemplateRows = 'repeat(' + (this.#fieldSize + 1) + ', ' + this.#cellSize + ')';
        this.#battlefieldDiv.style.gap = '2px';

        for (let i = 1; i < (this.#fieldSize + 1); i++) {
            const descrX = this.createDescr(this.#prefix + 'dx_' + i, this.#letters.get(i), 1, (i + 1));
            this.#battlefieldDiv.appendChild(descrX);

            const descrY = this.createDescr(this.#prefix + 'dy_' + i, '' + i, (i + 1), 1);
            this.#battlefieldDiv.appendChild(descrY);
        }

        for (let i = 0; i < this.#fieldSize; i++) {
            for (let j = 0; j < this.#fieldSize; j++) {
                const blockDiv = this.createBlock(this.#prefix + 'b_' + i + '_' + j, i + 2, j + 2);
                this.#battlefieldDiv.appendChild(blockDiv);
            }
        }

        if (holder.children.length > 0) {
            holder.removeChild(holder.children[0]);
        }
        holder.appendChild(this.#battlefieldDiv);
    }

    setInactive(b) {
        if(b) {
            this.#battlefieldDiv.style.filter = 'grayscale(1)';
        } else {
            this.#battlefieldDiv.style.filter = 'grayscale(0)';
        }
    }

    getBlock(e) {
        return new Block(e, this.#letters, this.#blockBasicClass);
    }

    isFieldBlock(e) {
        return e && e.id && e.parentElement === this.#battlefieldDiv && /^\w*\|b_\d+_\d+/.test(e.id);
    }

    isEmptyFieldBlock(e) {
        return this.isFieldBlock(e) && (e.className === 'block' || e.className === 'blockMarked');
    }

    createDescr(id, text, x, y) {
        const d = document.createElement('div');
        d.id = id;
        d.className = 'desc';
        d.style.gridColumn = '' + x;
        d.style.gridRow = '' + y;
        d.appendChild(document.createTextNode(text));
        return d;
    }

    createBlock(id, x, y) {
        const b = document.createElement('div');
        b.id = id;
        b.className = this.#blockBasicClass;
        b.style.gridColumnStart = '' + y;
        b.style.gridColumnEnd = '' + y;
        b.style.gridRowStart = '' + x;
        b.style.gridRowEnd = '' + x;
        return b;
    }

    getBlockDiv(x, y) {
        return document.getElementById(this.#prefix + 'b_' + x + '_' + y);
    }

    getLetter(x) {
        return this.#letters.get(x);
    }

    drawShips(matrix, shipClass) {
        if (!shipClass) {
            shipClass = 'ship';
        }
        matrix = structuredClone(matrix);
        for (let i = 0; i < this.#fieldSize; i++) {
            for (let j = 0; j < this.#fieldSize; j++) {
                const block = this.getBlockDiv(i, j);
                if(!block) {
                    continue;
                }
                block.appendChild(this.newSpan(i, j));
                if (matrix[i][j] !== 0) {
                    block.className = shipClass;
                    const shipSize = matrix[i][j];
                    const direction = this.getDirection(matrix, i, j);

                    block.style.display = 'flex';
                    block.style.alignItems = 'center';
                    block.style.flexDirection = (direction === 'vertical') ? 'column' : 'row';
                    block.style.justifyContent = (direction === 'vertical') ? 'space-between' : 'space-around';

                    if (shipSize > 1) {
                        for (let si = 1; si < shipSize; si++) {
                            if (direction === 'vertical') {
                                const removingBlock = this.getBlockDiv((i + si), j);
                                block.appendChild(this.newSpan((i + si), j));
                                if (removingBlock) {
                                    this.#battlefieldDiv.removeChild(removingBlock);
                                    matrix[i + si][j] = 0;
                                }
                            }
                            if (direction === 'horizontal') {
                                const removingBlock = this.getBlockDiv(i, (j + si));
                                block.appendChild(this.newSpan(i, j + si));
                                if(removingBlock) {
                                    this.#battlefieldDiv.removeChild(removingBlock);
                                    matrix[i][j + si] = 0;
                                }
                            }
                        }

                        if (direction === 'vertical') {
                            block.style.gridRowEnd = '' + (parseInt(block.style.gridRowStart) + shipSize);
                        }
                        if (direction === 'horizontal') {
                            block.style.gridColumnEnd = '' + (parseInt(block.style.gridColumn) + shipSize);
                        }
                    }
                }
            }
        }
    }

    getDirection(matrix, i, j) {
        if ((j + 1 < matrix.length) && (matrix[i][j + 1] === matrix[i][j])) {
            return 'horizontal';
        }

        return 'vertical';
    }

    drawShip(deckCount, x, y, direction) {
        const block = this.getBlockDiv(x, y);
        if (!block) {
            return;
        }
        block.className = 'ship';

        if (deckCount > 1) {
            for (let si = 1; si < deckCount; si++) {
                if (direction === 'vertical') {
                    this.#battlefieldDiv.removeChild(this.getBlockDiv(x + si, y));
                }
                if (direction === 'horizontal') {
                    this.#battlefieldDiv.removeChild(this.getBlockDiv(x, y + si));
                }
            }

            if (direction === 'vertical') {
                block.style.gridRowEnd = '' + (parseInt(block.style.gridRowStart) + deckCount);
            }
            if (direction === 'horizontal') {
                block.style.gridColumnEnd = '' + (parseInt(block.style.gridColumn) + deckCount);
            }
        }
    }

    drawBattlefield(matrix, shipClass = 'enemyShip') {
        for (let i = 0; i < matrix.length; i++) {
            for (let j = 0; j < matrix.length; j++) {
                const block = this.getBlockDiv(i, j);
                if (!block) {
                    continue;
                }

                if (matrix[i][j] === '.') {
                    block.appendChild(this.newDot(i, j))
                }
                if (matrix[i][j] === 'w') {
                    block.appendChild(this.getWounded())
                }
                if (matrix[i][j] === 'k') {
                    const direction = this.getDirection(matrix, i, j);
                    const deckCount = this.getDeckCount(matrix, i, j, direction);
                    block.className = shipClass;

                    if (deckCount > 1) {
                        for (let si = 1; si < deckCount; si++) {
                            if (direction === 'vertical') {
                                const blockDiv = this.getBlockDiv(i + si, j);
                                if (blockDiv) {
                                    this.#battlefieldDiv.removeChild(blockDiv);
                                }
                            }
                            if (direction === 'horizontal') {
                                const blockDiv = this.getBlockDiv(i, j + si);
                                if (blockDiv) {
                                    this.#battlefieldDiv.removeChild(blockDiv);
                                }
                            }
                        }

                        if (direction === 'vertical') {
                            block.style.gridRowEnd = '' + (parseInt(block.style.gridRowStart) + deckCount);
                        }
                        if (direction === 'horizontal') {
                            block.style.gridColumnEnd = '' + (parseInt(block.style.gridColumn) + deckCount);
                        }
                    }
                }
            }
        }
    }

    getDeckCount(matrix, x, y, direction) {
        let deckCount = 0;

        if (direction === 'vertical') {
            for (let i = 0; i < matrix.length - x; i++) {
                if (matrix[x + i][y] !== 'k') {
                    break;
                }
                deckCount++;
            }
        }

        if (direction === 'horizontal') {
            for (let i = 0; i < matrix.length - y; i++) {
                if (matrix[x][y + i] !== 'k') {
                    break;
                }
                deckCount++;
            }
        }

        return deckCount;
    }

    layoutBattlefield(matrix) {
        for (let i = 0; i < matrix.length; i++) {
            for (let j = 0; j < matrix.length; j++) {
                const span = this.getSpan(i, j);
                if (matrix[i][j] === '.') {
                    span.className = 'dot';
                }
                if (matrix[i][j] === 'w') {
                    span.appendChild(this.getWounded());
                }
                if (matrix[i][j] === 'k') {
                    span.appendChild(this.getWounded());
                }
            }
        }
    }

    getSpan(x, y) {
        return document.getElementById(this.#prefix + 's_' + x + '_' + y);
    }

    newSpan(x, y) {
        const span = document.createElement('span');
        span.id = this.#prefix + 's_' + x + '_' + y;
        return span;
    }

    newDot(x, y) {
        const span = this.newSpan(x, y);
        span.className = 'dot';
        return span;
    }

    getWounded() {
        return document.createTextNode("X");
    }
}

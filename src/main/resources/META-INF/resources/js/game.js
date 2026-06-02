class PositionConfig {
    #bordersAllowed;
    #cornersAllowed;

    constructor(bordersAllowed, cornersAllowed) {
        this.#bordersAllowed = bordersAllowed;
        this.#cornersAllowed = cornersAllowed;
    }

    borderCollisionAllowed() {
        return this.#bordersAllowed;
    }

    cornerCollisionAllowed() {
        return this.#cornersAllowed;
    }
}

class Matrix {

    // Размер поля
    #size;

    // Мапа настроек кораблей
    #shipConfig;

    // Настройки расположения кораблей
    #config;

    // Матрица расстановки
    #matrix;

    // Мапа кораблей, чтоб не пробегать всю матрицу,
    // а просто смотреть сколько кораблей какого типа уже создано
    #ships;

    constructor(size, shipConfig, config) {
        this.#size = size;
        this.#config = config;

        this.#matrix = [];
        for (let i = 0; i < this.#size; i++) {
            this.#matrix.push([]);
            for (let j = 0; j < this.#size; j++) {
                this.#matrix[i].push(0);
            }
        }

        this.#ships = new Map();

        const ships = shipConfig.split(';');
        this.#shipConfig = new Map();
        for (const ship of ships) {
            const splits = ship.split('x');
            if(!this.#shipConfig.get(parseInt(splits[0]))) {
                this.#shipConfig.set(parseInt(splits[0]), parseInt(splits[1]));
            } else {
                this.#shipConfig.set(parseInt(splits[0]), this.#shipConfig.get(parseInt(splits[0])) + parseInt(splits[1]));
            }
            this.#ships.set(parseInt(splits[0]), 0);
        }
    }

    getShipConfig() {
        return this.#shipConfig;
    }

    hasFreeCells(deckCount) {
        for (let i = 0; i < this.#size; i++) {
            for (let j = 0; j < this.#size; j++) {
                if (this.canPlaceShip(deckCount, i, j, 'horizontal')
                    || this.canPlaceShip(deckCount, i, j, 'vertical')) {
                    return true;
                }
            }
        }

        return false;
    }

    canPlaceShip(deckCount, x, y, direction) {
        if (!this.#shipConfig.get(deckCount)) {
            return false;
        }

        if (this.#ships.get(deckCount) >= this.#shipConfig.get(deckCount)) {
            return false;
        }

        // Проверка выхода за границы
        if (!this.pointInMatrix(x, y)) {
            return false;
        }
        if ((direction === 'vertical') && !this.pointInMatrix(x + deckCount - 1, y)) {
            return false;
        }

        if ((direction === 'horizontal') && !this.pointInMatrix(x, y + deckCount - 1)) {
            return false;
        }

        // Проверка коллизий границ
        if (!this.#config.borderCollisionAllowed()) {
            if (x === 0
                || y === 0
                || x === (this.#size - 1)
                || y === (this.#size - 1)
                || ((direction === 'vertical') && (x + deckCount) === this.#size)
                || ((direction === 'horizontal') && (y + deckCount) === this.#size)) {
                return false;
            }
        }

        // Проверка коллизий углов
        if (!this.#config.cornerCollisionAllowed()) {
            if (this.hasCornerCollision(x, y)) {
                return false
            }

            if (direction === 'vertical') {
                if (this.hasCornerCollision(x + deckCount - 1, y)) {
                    return false;
                }
            }

            if (direction === 'horizontal') {
                if (this.hasCornerCollision(x, y + deckCount - 1)) {
                    return false;
                }
            }
        }

        // Проверка коллизий по вертикали и горизонтали
        for (let deckIndex = 0; deckIndex < deckCount; deckIndex++) {
            if (direction === 'vertical') {
                if (this.#matrix[x + deckIndex][y] !== 0) {
                    return false;
                }

                if ((deckIndex === 0) && this.hasTopCollision(x + deckIndex, y)) {
                    return false;
                }

                // Корабли не должны касаться друг друга по бокам
                if (this.hasLeftRightCollisions(x + deckIndex, y)) {
                    return false;
                }

                if ((deckIndex === deckCount - 1) && this.hasBottomCollision(x + deckIndex, y)) {
                    return false;
                }
            }
            if (direction === 'horizontal') {
                if (this.#matrix[x][y + deckIndex] !== 0) {
                    return false;
                }

                if ((deckIndex === 0) && this.hasLeftCollision(x, y + deckIndex)) {
                    return false;
                }
                // Корабли не должны касаться друг друга сверху и снизу
                if (this.hasTopBottomCollisions(x, y + deckIndex)) {
                    return false;
                }

                if ((deckIndex === deckCount - 1) && this.hasRightCollision(x, y + deckIndex)) {
                    return false;
                }
            }
        }
        return true;
    }

    hasCornerCollision(x, y) {
        return (this.pointInMatrix(x + 1, y + 1) && this.#matrix[x + 1][y + 1] !== 0)
            || (this.pointInMatrix(x + 1, y - 1) && this.#matrix[x + 1][y - 1] !== 0)
            || (this.pointInMatrix(x - 1, y - 1) && this.#matrix[x - 1][y - 1] !== 0)
            || (this.pointInMatrix(x - 1, y + 1) && this.#matrix[x - 1][y + 1] !== 0);
    }

    hasTopBottomCollisions(x, y) {
        return this.hasTopCollision(x, y) || this.hasBottomCollision(x, y);
    }

    hasTopCollision(x, y) {
        if (this.pointInMatrix(x - 1, y) && this.#matrix[x - 1][y] !== 0) {
            return true;
        }
    }

    hasBottomCollision(x, y) {
        return !!(this.pointInMatrix(x + 1, y) && this.#matrix[x + 1][y] !== 0);
    }

    hasLeftRightCollisions(x, y) {
        return this.hasLeftCollision(x, y) || this.hasRightCollision(x, y);
    }

    hasLeftCollision(x, y) {
        return !!(this.pointInMatrix(x, y - 1) && this.#matrix[x][y - 1] !== 0);
    }

    hasRightCollision(x, y) {
        return !!(this.pointInMatrix(x, y + 1) && this.#matrix[x][y + 1] !== 0);
    }

    pointInMatrix(x, y) {
        return ((x > -1) && (x < this.#matrix.length)) && ((y > -1) && (y < this.#matrix.length));
    }

    placeShip(deckCount, x, y, direction) {
        if (this.canPlaceShip(deckCount, x, y, direction)) {
            this.#ships.set(deckCount, this.#ships.get(deckCount) + 1);

            for (let deckIndex = 0; deckIndex < deckCount; deckIndex++) {
                if (direction === 'vertical') {
                    this.#matrix[x + deckIndex][y] = deckCount;
                }
                if (direction === 'horizontal') {
                    this.#matrix[x][y + deckIndex] = deckCount;
                }
            }
        }
    }

    isFull() {
        let fieldIsFull = true;
        for (let [ship, count] of this.#ships) {
            fieldIsFull &= this.#shipConfig.get(ship) === count;
        }

        return fieldIsFull;
    }

    getSize() {
        return this.#size;
    }

    getMatrix() {
        return this.#matrix;
    }
}

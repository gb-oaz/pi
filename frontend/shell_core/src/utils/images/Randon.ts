// assets/card/index.ts
import cardImage1 from "../../assets/card/gb_cb.png";
import cardImage2 from "../../assets/card/gb_cp.png";
import cardImage3 from "../../assets/card/gb_qb.png";
import cardImage4 from "../../assets/card/gb_qp.png";

type RandomImageAndColor = {
    getRandomImage: () => string;
    getRandomColor: () => string;
};

const cardImages = [cardImage1, cardImage2, cardImage3, cardImage4];
const availableColors = [
    'red', 'pink', 'purple', 'deep-purple', 'indigo', 'blue', 'light-blue',
    'cyan', 'teal', 'green', 'light-green', 'lime', 'yellow', 'amber',
    'orange', 'deep-orange', 'brown', 'grey', 'blue-grey'
];

const getRandomIndex = (max: number) => Math.floor(Math.random() * max);
const getRandomImage = () => cardImages[getRandomIndex(cardImages.length)];
const getRandomColor = () => `${availableColors[getRandomIndex(availableColors.length)]}-${Math.floor(Math.random() * 6) + 5}`;

export const random: RandomImageAndColor = {
    getRandomImage,
    getRandomColor
};
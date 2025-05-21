// assets/card/index.ts
import cardImage1 from "../../assets/card/gb_cb.png";
import cardImage2 from "../../assets/card/gb_cp.png";
import cardImage3 from "../../assets/card/gb_qb.png";
import cardImage4 from "../../assets/card/gb_qp.png";

import background1 from "../../assets/background/b_cb.png";
import background2 from "../../assets/background/b_cp.png";
import background3 from "../../assets/background/b_qb.png";
import background4 from "../../assets/background/b_qp.png";

type RandomImageAndColor = {
    getRandomImage: () => string;
    getRandomColor: () => string;
    getRandomBackground: () => string;
    getFormStatusColor: (form: Record<string, string>, requiredFields?: string[]) => string;
    backgroundImages: string[];
};

const cardImages = [cardImage1, cardImage2, cardImage3, cardImage4];
const backgroundImages = [background1, background2, background3, background4];
const availableColors = [
    'red', 'pink', 'purple', 'deep-purple', 'indigo', 'blue', 'light-blue',
    'cyan', 'teal', 'green', 'light-green', 'lime', 'yellow', 'amber',
    'orange', 'deep-orange', 'brown', 'grey', 'blue-grey'
];

const getFormStatusColor = (form: Record<string, string>, requiredFields?: string[]) => {
    const fields = requiredFields || Object.keys(form);
    const filledFields = fields.filter(field => form[field] && form[field].trim() !== '').length;

    if (filledFields === 0) {
        return 'grey-6'; // Não começado
    } else if (filledFields < fields.length) {
        return 'yellow-6'; // Preenchendo
    } else {
        return 'green-6'; // Completo
    }
};

const getRandomIndex = (max: number) => Math.floor(Math.random() * max);
const getRandomImage = () => cardImages[getRandomIndex(cardImages.length)];
const getRandomBackground = () => backgroundImages[getRandomIndex(backgroundImages.length)];
const getRandomColor = () => `${availableColors[getRandomIndex(availableColors.length)]}-${Math.floor(Math.random() * 6) + 5}`;

export const random: RandomImageAndColor = {
    getRandomImage,
    getRandomColor,
    getRandomBackground,
    getFormStatusColor,
    backgroundImages
};
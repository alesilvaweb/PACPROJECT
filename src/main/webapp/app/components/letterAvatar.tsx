import React from 'react';

const getPercent = (percent, value) => (value * percent) / 100;

const colorTypes = new Map([
  ['r', 'rgb'],
  ['#', 'hex'],
  ['h', 'hsla'],
]);

const getColorType = color => colorTypes.get(color[0]);
const getArrayValuesFromString = color => color.replace(/\W/gi, '').split('');

const reverseHexShort = color =>
  color.reduce((acc, num) => {
    acc.push(num);
    acc.push(num);
    return acc;
  }, []);

const transformHexValues = color =>
  color.reduce((acc, value, index) => {
    if (!(index % 2)) return acc;

    const decimalValue = parseInt(`${color[index - 1]}${value}`, 16);

    return acc.push(decimalValue), acc;
  }, []);

const convertColorHexToRGBArray = color => {
  let normalizedHexColor;
  const hexColorArray = getArrayValuesFromString(color);

  if (hexColorArray.length <= 4) {
    normalizedHexColor = reverseHexShort(hexColorArray);
  }

  return transformHexValues(normalizedHexColor || hexColorArray);
};

const isOpacityTransparent = opacity => {
  if (!opacity) return false;
  let opacityValue = opacity;

  if (Math.floor(opacity) === 0) {
    opacityValue = Math.floor(opacity * 254);
  }

  return opacityValue > 70;
};

const getContrastRatio = rgbColorArray =>
  Math.round((parseInt(rgbColorArray[0]) * 299 + parseInt(rgbColorArray[1]) * 587 + parseInt(rgbColorArray[2]) * 114) / 1000);

const getTextColor = color => {
  let rgbColorArray;
  const colorType = getColorType(color);

  if (!colorType || colorType === 'hsla') {
    console.warn(
      new Error(`Unssuported color:
    if you are using a hsla color, is not supported due contrast ratio.
    also be careful with gradient background not suported by svgs.
    `)
    );
    return 'black';
  }

  if (colorType === 'hex') {
    rgbColorArray = convertColorHexToRGBArray(color);
  }

  if (colorType === 'rgb') {
    rgbColorArray = color
      .replace(/((rgb|a)|(\(|\)))/g, '')
      .split(',')
      .map(str => +str.trim());
  }

  if (isOpacityTransparent(rgbColorArray[3])) {
    return 'black';
  }

  return getContrastRatio(rgbColorArray) > 128 ? 'black' : 'white';
};

export const LetterAvatar = props => {
  const size = getPercent(32, 64);
  const textColor = getTextColor('#ffffff');

  return (
    <svg height={42} width={42}>
      <circle cx={size} cy={size} r={size} fill={'#d4d4d4'} />
      <text
        fontFamily="Arial, Helvetica, sans-serif"
        fontWeight={700}
        fontSize={size}
        textAnchor="middle"
        fill={textColor}
        x={size}
        y={getPercent(59, 48)}
      >
        {props.children}
      </text>
    </svg>
  );
};

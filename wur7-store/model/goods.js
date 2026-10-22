import { genGood } from './good';
import { genCourse } from './good_course';


export function getGoodsList(tab, baseID = 0, length = 10) {
  switch (tab) {
    case 0:
      return new Array(length).fill(0).map((_, idx) => genCourse(idx + baseID));
    case 1:
      return new Array(length).fill(0).map((_, idx) => genCourse(idx + baseID));
    case 2:
      return new Array(length).fill(0).map((_, idx) => genGood(idx + baseID));

    default:
      return new Array(length).fill(0).map((_, idx) => genCourse(idx + baseID));
  }
}

export const goodsList = getGoodsList();


export function getGoodItem(tab, ID = 0) {
  switch (tab) {
    case 0:
      return genCourse(ID);
    case 1:
      return genCourse(ID);
    case 2:
      return genGood(ID);
    default:
      return genCourse(ID);
  }
}

let addressPromise = [];



/** 用户保存了一个地址 */
export const resolveAddress = (address) => {
  const allAddress = [...addressPromise];
  addressPromise = [];

  console.info('用户保存了一个地址', address);

  allAddress.forEach(({ resolver }) => resolver(address));
};

/** 取消编辑 */
export const rejectAddress = () => {
  const allAddress = [...addressPromise];
  addressPromise = [];

  allAddress.forEach(({ rejecter }) => rejecter(new Error('cancel')));
};

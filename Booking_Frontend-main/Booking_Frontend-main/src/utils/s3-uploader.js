import AWS from 'aws-sdk';

const s3 = new AWS.S3({
  region: 'eu-north-1',
  credentials: {
    accessKeyId: 'AKIAX2DZEHWGJAIWA54C',
    secretAccessKey: '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'
  }
});

export const uploadImageToS3 = async (file) => {
  const params = {
    Bucket: 'booking-image-upload-bucket',
    Key: `products/${Date.now()}-${file.name}`,
    Body: file,
    ContentType: file.type

  };

  try {
    const data = await s3.upload(params).promise();
    return data.Location;
  } catch (error) {
    console.error('Error uploading to S3:', error);
    throw error;
  }
};

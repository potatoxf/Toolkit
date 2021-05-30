package pxf.toolkit.extension.video;

import java.io.File;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import pxf.toolkit.basic.util.Empty;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;

/**
 * 视频工具类
 *
 * @author potatoxf
 * @date 2021/3/6
 */
public final class VideoHelper {

  private VideoHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 默认压缩视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @throws EncoderException 当编码发生异常
   */
  public static void defaultCompressionVideo(String source, String target) throws EncoderException {
    defaultCompressionVideo(new File(source), new File(target));
  }

  /**
   * 默认压缩视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @throws EncoderException 当编码发生异常
   */
  public static void defaultCompressionVideo(Path source, Path target) throws EncoderException {
    defaultCompressionVideo(source.toFile(), target.toFile());
  }

  /**
   * 默认压缩视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @throws EncoderException 当编码发生异常
   */
  public static void defaultCompressionVideo(File source, File target) throws EncoderException {
    //  视频属性设置
    int maxBitRate = 128000;
    int maxSamplingRate = 44100;
    int bitRate = 800000;
    int maxFrameRate = 20;
    int maxWidth = 1280;
    handleSingleVideo(
        source,
        target,
        (audioInfo, audioAttributes) -> {
          // 设置通用编码格式
          audioAttributes.setCodec("aac");
          // 设置最大值：比特率越高，清晰度/音质越好
          // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 128kb)
          if (audioInfo.getBitRate() > maxBitRate) {
            audioAttributes.setBitRate(maxBitRate);
          }
          // 设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
          audioAttributes.setChannels(audioInfo.getChannels());
          // 设置编码时候的音量值，未设置为0,如果256，则音量值不会改变
          // audio.setVolume(256);
          if (audioInfo.getSamplingRate() > maxSamplingRate) {
            audioAttributes.setSamplingRate(maxSamplingRate);
          }
        },
        (videoInfo, videoAttributes) -> {
          videoAttributes.setCodec("h264");
          // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 800000 = 800kb)
          if (videoInfo.getBitRate() > bitRate) {
            videoAttributes.setBitRate(bitRate);
          }
          // 视频帧率：15 f / s 帧率越低，效果越差
          // 设置视频帧率（帧率越低，视频会出现断层，越高让人感觉越连续），视频帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per
          // Second，简：FPS）或“赫兹”（Hz）。
          if (videoInfo.getFrameRate() > maxFrameRate) {
            videoAttributes.setFrameRate(maxFrameRate);
          }
          // 限制视频宽高
          int width = videoInfo.getSize().getWidth();
          int height = videoInfo.getSize().getHeight();
          if (width > maxWidth) {
            float rat = (float) width / maxWidth;
            videoAttributes.setSize(new VideoSize(maxWidth, (int) (height / rat)));
          }
        },
        Empty.biConsumer());
  }

  /**
   * 处理单个视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @param configAudioAttributes 配置声音属性
   * @param configVideoAttributes 配置视频属性
   * @param configEncodingAttributes 配置编码属性
   * @throws EncoderException 当编码发生异常
   */
  public static void handleSingleVideo(
      String source,
      String target,
      BiConsumer<AudioInfo, AudioAttributes> configAudioAttributes,
      BiConsumer<VideoInfo, VideoAttributes> configVideoAttributes,
      BiConsumer<MultimediaInfo, EncodingAttributes> configEncodingAttributes)
      throws EncoderException {
    handleSingleVideo(
        new File(source),
        new File(target),
        configAudioAttributes,
        configVideoAttributes,
        configEncodingAttributes);
  }

  /**
   * 处理单个视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @param configAudioAttributes 配置声音属性
   * @param configVideoAttributes 配置视频属性
   * @param configEncodingAttributes 配置编码属性
   * @throws EncoderException 当编码发生异常
   */
  public static void handleSingleVideo(
      Path source,
      Path target,
      BiConsumer<AudioInfo, AudioAttributes> configAudioAttributes,
      BiConsumer<VideoInfo, VideoAttributes> configVideoAttributes,
      BiConsumer<MultimediaInfo, EncodingAttributes> configEncodingAttributes)
      throws EncoderException {
    handleSingleVideo(
        source.toFile(),
        target.toFile(),
        configAudioAttributes,
        configVideoAttributes,
        configEncodingAttributes);
  }

  /**
   * 处理单个视频
   *
   * @param source 原视频
   * @param target 目标视频
   * @param configAudioAttributes 配置声音属性
   * @param configVideoAttributes 配置视频属性
   * @param configEncodingAttributes 配置编码属性
   * @throws EncoderException 当编码发生异常
   */
  public static void handleSingleVideo(
      File source,
      File target,
      BiConsumer<AudioInfo, AudioAttributes> configAudioAttributes,
      BiConsumer<VideoInfo, VideoAttributes> configVideoAttributes,
      BiConsumer<MultimediaInfo, EncodingAttributes> configEncodingAttributes)
      throws EncoderException {
    MultimediaObject multimediaObject = new MultimediaObject(source);
    MultimediaInfo multimediaInfo = multimediaObject.getInfo();

    AudioAttributes audioAttributes = new AudioAttributes();
    configAudioAttributes.accept(multimediaInfo.getAudio(), audioAttributes);

    VideoAttributes videoAttributes = new VideoAttributes();
    configVideoAttributes.accept(multimediaInfo.getVideo(), videoAttributes);

    EncodingAttributes encodingAttributes = new EncodingAttributes();
    encodingAttributes
        .setOutputFormat(multimediaInfo.getFormat())
        .setInputFormat(multimediaInfo.getFormat())
        .setAudioAttributes(audioAttributes)
        .setVideoAttributes(videoAttributes);

    configEncodingAttributes.accept(multimediaInfo, encodingAttributes);
    Encoder encoder = new Encoder();

    encoder.encode(multimediaObject, target, encodingAttributes);
  }
}

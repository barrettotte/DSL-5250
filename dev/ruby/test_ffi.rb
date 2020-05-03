require 'ffi' # gem install ffi

module Win
  extend FFI::Library
  
  class SystemTime < FFI::Struct
    layout :year, :ushort,
           :month, :ushort,
           :day_of_week, :ushort,
           :day, :ushort,
           :hour, :ushort,
           :minute, :ushort,
           :second, :ushort,
           :millis, :ushort
  end
  ffi_lib 'kernel32'
  ffi_convention :stdcall
  
  attach_function :GetLocalTime, [ :pointer ], :void
end
  
mytime = Win::SystemTime.new
Win.GetLocalTime(mytime)
  
args = [
  mytime[:month], mytime[:day], mytime[:year],
  mytime[:hour], mytime[:minute], mytime[:second]
]
 
puts "Date: %u/%u/%u\nTime: %02u:%02u:%02u" % args

# https://github.com/ffi/ffi/wiki/Windows-Examples

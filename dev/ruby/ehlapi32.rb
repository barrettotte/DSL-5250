require 'fiddle'
require 'fiddle/import'

# testing out with ruby, just because

module Kernel32
  extend Fiddle::Importer
  dlload 'kernel32'

  extern 'int GetCurrentProcessId()'
end

module Ehlapi32
  extend Fiddle::Importer
  dlload  File.expand_path('./ehlapi32.dll', File.dirname(__FILE__))

  extern 'int WD_ConnectPS(int hInstance, string ShortName)'
end

process = Kernel32.GetCurrentProcessId()
status = Ehlapi32.WD_ConnectPS(process, 'A')

puts "process: #{process}"
puts "status: #{status}"

# aw same issue...go figure
